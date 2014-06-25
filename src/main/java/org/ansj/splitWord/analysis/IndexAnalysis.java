package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.impl.GetWordsImpl;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.Forest;

/**
 * 用于检索的分词方式
 * 
 * @author ansj
 * 
 */
public class IndexAnalysis extends Analysis {

	@Override
	protected List<Term> getResult(final Graph graph) {
		Merger merger = new Merger() {

			@Override
			public List<Term> merger() {
				graph.walkPath();

				// 数字发现
				if (MyStaticValue.isNumRecognition)
					NumRecognition.recognition(graph.terms);

				// 姓名识别
				if (MyStaticValue.isNameRecognition) {
					new AsianPersonRecognition(graph.terms).recognition();
					// 外国人名识别
					new ForeignPersonRecognition(graph.terms).recognition();
				}

				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms, forests).recognition();

				return result();
			}

			/**
			 * 检索的分词
			 * 
			 * @return
			 */
			private List<Term> result() {
				// TODO Auto-generated method stub
				List<Term> result = new LinkedList<Term>();
				List<Term> last = new LinkedList<Term>();
				Term term = null;
				String temp = null;
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					term = graph.terms[i];
					if (term == null) {
						continue;
					}

					result.add(term);
				}
				
				
				for (int i = 0; i < length; i++) {
					term = graph.terms[i];
					if (term == null) {
						continue;
					}

					term = term.getNext();

					while (term != null) {
						result.add(term);
						temp = term.getName();
						term = term.getNext();
						if (term == null || term.getName().length() == 1 || temp.equals(term.getName())) {
							break;
						}
					}
				}

				for (Term term2 : result) {
					if (term2.getName().length() >= 3) {
						GetWordsImpl gwi = new GetWordsImpl(term2.getName());
						while ((temp = gwi.allWords()) != null) {
							if (temp.length() > 1 && temp.length() < term2.getName().length()) {
								last.add(new Term(temp, gwi.offe + term2.getOffe(), TermNatures.NULL));
							}
						}
					}
				}

				result.addAll(last);

				setRealName(graph, result);
				return result;
			}
		};

		return merger.merger();
	}

	private IndexAnalysis() {
	};

	public IndexAnalysis(Forest... forests) {
		// TODO Auto-generated constructor stub
		this.forests = forests;
	}

	public IndexAnalysis(Reader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnsjReader(reader));
	}

	public static List<Term> parse(String str) {
		return new IndexAnalysis().parseStr(str);
	}

	public static List<Term> parse(String str, Forest... forests) {
		return new IndexAnalysis(forests).parseStr(str);

	}
}
