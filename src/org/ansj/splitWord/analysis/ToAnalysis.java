package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.util.Graph;
import org.ansj.util.recognition.AsianPersonRecognition;
import org.ansj.util.recognition.NumRecognition;
import org.ansj.util.recognition.UserDefineRecognition;

/**
 * 标准分词
 * 
 * @author ansj
 * 
 */
public class ToAnalysis extends Analysis {

	public ToAnalysis(Reader reader) {
		super(reader);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected List<Term> getResult(final Graph graph) {
		// TODO Auto-generated method stub
		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {
				// TODO Auto-generated method stub
				// 最短路径?我奇怪加上这个应该快了.但是相反.我按照常理处理吧...*
				graph.walkPath();

				// 数字发现
				if (graph.hasNum) {
					NumRecognition.recogntionNM(graph.terms);
				}

				// 姓名识别
				if (graph.hasPerson) {
					new AsianPersonRecognition(graph.terms).recogntion();
					graph.walkPathByScore();
				}

				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms).recongnitionTerm();
				graph.rmLittlePath();
				graph.walkPathByFreq();

				return getResult();
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
				return result;
			}
		};
		return merger.merger();
	}

	private ToAnalysis() {
	};

	public static List<Term> paser(String str) {
		return new ToAnalysis().paserStr(str);
	}
}
