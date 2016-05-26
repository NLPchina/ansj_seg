package org.ansj.splitWord.analysis;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.arrimpl.AsianPersonRecognition;
import org.ansj.recognition.arrimpl.ForeignPersonRecognition;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.ansj.util.NameFix;
import org.ansj.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.domain.Forest;

/**
 * 默认用户自定义词性优先
 * 
 * @author ansj
 *
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

				// 用户自定义词典的识别
				userDefineRecognition(graph, forests);

				// 数字发现
				if (MyStaticValue.isNumRecognition && graph.hasNum) {
					new NumRecognition().recognition(graph.terms);
				}

				// 姓名识别
				if (graph.hasPerson && MyStaticValue.isNameRecognition) {
					// 亚洲人名识别
					new AsianPersonRecognition().recognition(graph.terms);
					graph.walkPathByScore();
					NameFix.nameAmbiguity(graph.terms);
					// 外国人名识别
					new ForeignPersonRecognition().recognition(graph.terms);
					graph.walkPathByScore();
				}

				return getResult();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(InsertTermType.REPLACE, forests).recognition(graph.terms);
				graph.rmLittlePath();
				graph.walkPathByScore();
				graph.rmLittlePath();
			}

			private List<Term> getResult() {
				// TODO Auto-generated method stub
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

	/**
	 * 用户自己定义的词典
	 * 
	 * @param forest
	 */
	public DicAnalysis(Forest... forests) {
		if (forests == null) {
			forests = new Forest[] { UserDefineLibrary.FOREST };
		}
		this.forests = forests;
	}

	public DicAnalysis(BufferedReader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnsjReader(reader));
	}

	public static Result parse(String str) {
		return new DicAnalysis().parseStr(str);
	}

	public static Result parse(String str, Forest... forests) {
		return new DicAnalysis(forests).parseStr(str);
	}
}
