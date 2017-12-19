package org.ansj.splitWord.analysis;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.recognition.arrimpl.PersonRecognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil;
import org.ansj.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认用户自定义词性优先
 *
 * @author ansj
 */
public class DicAnalysis extends Analysis {

	@Override
	protected List<Term> getResult(final Graph graph) {

		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				graph.walkPath();

				// 数字发现
				if (isNumRecognition) {
					new NumRecognition(isQuantifierRecognition && graph.hasNumQua).recognition(graph);
				}

				// 姓名识别
				if (graph.hasPerson && isNameRecognition) {
					// 人名识别
					new PersonRecognition().recognition(graph);
					graph.walkPathByScore();
					graph.walkPathByScore();
				}

				return getResult();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {

				if (forests == null) {
					return;
				}

				int beginOff = graph.terms[0].getOffe();

				Forest forest = null;
				for (int i = forests.length - 1; i >= 0; i--) {
					forest = forests[i];
					if (forest == null) {
						continue;
					}

					GetWord word = forest.getWord(graph.chars);
					String temp = null;
					int tempFreq = 50;
					while ((temp = word.getAllWords()) != null) {
						Term tempTerm = graph.terms[word.offe];
						tempFreq = getInt(word.getParam()[1], 50);
						if (graph.terms[word.offe] != null && graph.terms[word.offe].getName().equals(temp)) {
							TermNatures termNatures = new TermNatures(new TermNature(word.getParam()[0],tempFreq),tempFreq, -1);
							tempTerm.updateTermNaturesAndNature(termNatures);
						} else {
							Term term = new Term(temp, beginOff + word.offe, word.getParam()[0], tempFreq);
							term.selfScore(-1 * Math.pow(Math.log(tempFreq), temp.length()));
							TermUtil.insertTerm(graph.terms, term, InsertTermType.REPLACE);
						}
					}
				}

				graph.rmLittlePath();
				graph.walkPathByScore();
				graph.rmLittlePath();
			}

			private int getInt(String str, int def) {
				try {
					return Integer.parseInt(str);
				} catch (NumberFormatException e) {
					return def;
				}
			}

			private List<Term> getResult() {

				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					Term term = graph.terms[i];
					if (term != null) {
						setIsNewWord(term);
						result.add(term);
					}
				}
				setRealName(graph, result);
				return result;
			}
		};
		return merger.merger();
	}

	public DicAnalysis() {
		super();
	}

	public DicAnalysis(Reader reader) {
		super.resetContent(new AnsjReader(reader));
	}

	public static Result parse(String str) {
		return new DicAnalysis().parseStr(str);
	}

	public static Result parse(String str, Forest... forests) {
		return new DicAnalysis().setForests(forests).parseStr(str);
	}
}
