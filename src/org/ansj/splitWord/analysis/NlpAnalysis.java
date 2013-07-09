package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.Graph;

/**
 * 自然语言分词,结果不稳定但是很全面
 * 
 * @author ansj
 * 
 */
public class NlpAnalysis extends Analysis {

	public NlpAnalysis(Reader reader, LearnTool learn) {
		super(reader);
		this.learn = learn;
	}

	private LearnTool learn = null;

	@Override
	protected List<Term> getResult(final Graph graph) {
		// TODO Auto-generated method stub
		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {
				// TODO Auto-generated method stub
				graph.walkPath();

				// 数字发现
				if (graph.hasNum) {
					NumRecognition.recognition(graph.terms);
				}

				// 词性标注
				List<Term> result = getResult();
				new NatureRecognition(result).recognition();
				
				
				// 新词发现训练
				learn.learn(graph);

				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms).recognition();
//				graph.rmLittlePath();
				graph.walkPathByScore();

				
				// 进行新词发现
				new NewWordRecognition(graph.terms, learn).recognition();
				graph.walkPathByScore();

				//优化后重新获得最优路径
				result = getResult();
				
				return result;
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

	/**
	 * 传入学习机
	 * 
	 * @param learnRecognition
	 */
	private NlpAnalysis(LearnTool learn) {
		this.learn = learn;
	};

	public static List<Term> parse(String str, LearnTool learn) {
		return new NlpAnalysis(learn).parseStr(str);
	}
	
	public static List<Term> parse(String str) {
		return new NlpAnalysis(new LearnTool()).parseStr(str);
	}
}
