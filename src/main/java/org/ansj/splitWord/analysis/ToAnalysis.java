package org.ansj.splitWord.analysis;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.recognition.arrimpl.PersonRecognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 标准分词
 *
 * @author ansj
 */
public class ToAnalysis extends Analysis {

	@Override
	protected List<Term> getResult(final Graph graph) {

		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {

				graph.walkPath();

				// 姓名识别
				if (graph.hasPerson && isNameRecognition) {
					// 人名识别
					new PersonRecognition().recognition(graph);
				}

				// 数字发现
				if (isNumRecognition) {
					new NumRecognition(isQuantifierRecognition && graph.hasNumQua).recognition(graph);
				}

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				return getResult();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(InsertTermType.SKIP, forests).recognition(graph);
				graph.rmLittlePath();
				graph.walkPathByScore();
			}

			private List<Term> getResult() {
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						setIsNewWord(graph.terms[i]) ;
						result.add(graph.terms[i]);
					}
				}
				setRealName(graph, result);
				return result;
			}
		};
		return merger.merger();
	}

	public ToAnalysis() {
		super();
	}

	public ToAnalysis(Reader reader) {
		super.resetContent(new AnsjReader(reader));
	}

	public static Result parse(String str) {
		return new ToAnalysis().parseStr(str);
	}

	public static Result parse(String str, Forest... forests) {
		return new ToAnalysis().setForests(forests).parseStr(str);
	}

}
