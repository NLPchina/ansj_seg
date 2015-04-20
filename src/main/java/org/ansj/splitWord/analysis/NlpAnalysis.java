package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.app.crf.SplitWord;
import org.ansj.dic.LearnTool;
import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.library.DATDictionary;
import org.ansj.library.NatureLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.ansj.util.NameFix;
import org.ansj.util.WordAlert;
import org.nlpcn.commons.lang.tire.domain.Forest;

/**
 * 自然语言分词,具有未登录词发现功能。建议在自然语言理解中用。搜索中不要用
 * 
 * @author ansj
 * 
 */
public class NlpAnalysis extends Analysis {

	private LearnTool learn = null;

	private static final SplitWord DEFAULT_SLITWORD = MyStaticValue.getCRFSplitWord();

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

				if (learn == null) {
					learn = new LearnTool();
				}
				learn.learn(graph, DEFAULT_SLITWORD);

				// 通过crf分词
				List<String> words = DEFAULT_SLITWORD.cut(graph.chars);

				for (String word : words) {
					if (word.length() < 2 || DATDictionary.isInSystemDic(word) || WordAlert.isRuleWord(word)) {
						continue;
					}
					learn.addTerm(new NewWord(word, NatureLibrary.getNature("nw")));
				}

				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms, forests).recognition();
				graph.rmLittlePath();
				graph.walkPathByScore();

				// 进行新词发现
				new NewWordRecognition(graph.terms, learn).recognition();
				graph.walkPathByScore();

				// 修复人名左右连接
				NameFix.nameAmbiguity(graph.terms);

				// 优化后重新获得最优路径
				result = getResult();

				// 激活辞典
				for (Term term : result) {
					learn.active(term.getName());
				}

				setRealName(graph, result);

				return result;
			}

			private List<Term> getResult() {
				// TODO Auto-generated method stub
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] == null) {
						continue;
					}
					result.add(graph.terms[i]);
				}
				return result;
			}
		};
		return merger.merger();
	}

	private NlpAnalysis() {
	};

	/**
	 * 用户自己定义的词典
	 * 
	 * @param forest
	 */

	public NlpAnalysis(Forest... forests) {
		this.forests = forests;
	}

	public NlpAnalysis(LearnTool learn, Forest... forests) {
		this.forests = forests;
		this.learn = learn;
	}

	public NlpAnalysis(Reader reader, Forest... forests) {
		this.forests = forests;
		super.resetContent(new AnsjReader(reader));
	}

	public NlpAnalysis(Reader reader, LearnTool learn, Forest... forests) {
		this.forests = forests;
		this.learn = learn;
		super.resetContent(new AnsjReader(reader));
	}

	public static List<Term> parse(String str) {
		return new NlpAnalysis().parseStr(str);
	}

	public static List<Term> parse(String str, Forest... forests) {
		return new NlpAnalysis(forests).parseStr(str);
	}

	public static List<Term> parse(String str, LearnTool learn, Forest... forests) {
		return new NlpAnalysis(learn, forests).parseStr(str);
	}
}
