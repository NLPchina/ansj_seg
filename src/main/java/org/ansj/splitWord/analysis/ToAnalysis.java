package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.ansj.util.NameFix;
import org.nlpcn.commons.lang.tire.domain.Forest;

/**
 * 标准分词
 * 
 * @author ansj
 * 
 */
public class ToAnalysis extends Analysis {

	@Override
	protected List<Term> getResult(final Graph graph) {
		// TODO Auto-generated method stub
		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {
				graph.walkPath();
				// 数字发现
				if (MyStaticValue.isNumRecognition && graph.hasNum) {
					NumRecognition.recognition(graph.terms);
				}

				// 姓名识别
				if (graph.hasPerson && MyStaticValue.isNameRecognition) {
					// 亚洲人名识别
					new AsianPersonRecognition(graph.terms).recognition();
					graph.walkPathByScore();
					NameFix.nameAmbiguity(graph.terms);
					// 外国人名识别
					new ForeignPersonRecognition(graph.terms).recognition();
					graph.walkPathByScore();
				}

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				return getResult();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(graph.terms, forests).recognition();
				graph.rmLittlePath();
				graph.walkPathByScore();
			}

			private List<Term> getResult() {
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						result.add(graph.terms[i]);
					}
				}
				setRealName(graph, result);
				return result;
			}
		};
		return merger.merger();
	}

	private ToAnalysis() {
	};

	/**
	 * 用户自己定义的词典
	 * 
	 * @param forest
	 */
	public ToAnalysis(Forest... forests) {
		if (forests == null) {
			forests = new Forest[] { UserDefineLibrary.FOREST };
		}
		this.forests = forests;
	}

	public ToAnalysis(Reader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnsjReader(reader));
	}

	public static List<Term> parse(String str) {
		return new ToAnalysis().parseStr(str);
	}

	public static List<Term> parse(String str, Forest... forests) {
		return new ToAnalysis(forests).parseStr(str);
	}
}
