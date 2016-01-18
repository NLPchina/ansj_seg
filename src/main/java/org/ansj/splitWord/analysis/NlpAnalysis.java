package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.app.crf.SplitWord;
import org.ansj.dic.LearnTool;
import org.ansj.domain.AnsjItem;
import org.ansj.domain.Nature;
import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.DATDictionary;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.ansj.util.NameFix;
import org.ansj.util.TermUtil;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.MapCount;
import org.nlpcn.commons.lang.util.WordAlert;

/**
 * 自然语言分词,具有未登录词发现功能。建议在自然语言理解中用。搜索中不要用
 * 
 * @author ansj
 * 
 */
public class NlpAnalysis extends Analysis {

	private LearnTool learn = null;

	private static final String TAB = "\t";
	
	private static final int CRF_WEIGHT = 6 ;

	private static final SplitWord DEFAULT_SLITWORD = MyStaticValue.getCRFSplitWord();

	@Override
	protected List<Term> getResult(final Graph graph) {
		// TODO Auto-generated method stub

		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {

				if (learn == null) {
					learn = new LearnTool();
				}
				
				graph.walkPath();

				learn.learn(graph, DEFAULT_SLITWORD);

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

				MapCount<String> mc = new MapCount<String>();
				if (DEFAULT_SLITWORD != null) {

					// 通过crf分词
					List<String> words = DEFAULT_SLITWORD.cut(graph.chars);

					String temp = null;
					TermNatures tempTermNatures = null;
					int tempOff = 0;

					for (String word : words) {

						AnsjItem item = DATDictionary.getItem(word);
						Term term = null;
						if (item != AnsjItem.NULL) {
							term = new Term(word, tempOff, DATDictionary.getItem(word));
						} else {
							TermNatures termNatures = NatureRecognition.getTermNatures(word);
							if (termNatures != TermNatures.NULL) {
								term = new Term(word, tempOff, termNatures);
							} else {
								term = new Term(word, tempOff, TermNatures.NW);
							}
						}

						TermUtil.insertTerm(graph.terms, term, 2);

						tempOff += word.length();

						// 对于非词典中的词持有保守态度
						if (temp != null) {
							if (tempTermNatures != TermNatures.NW && term.termNatures() != TermNatures.NW) {
								mc.add(temp + TAB + word, CRF_WEIGHT);
							}
						} else if (term.termNatures() != TermNatures.NW) {
							mc.add("始##始" + TAB + word, CRF_WEIGHT);
						}

						temp = word;
						tempTermNatures = term.termNatures();

						if (word.length() < 2 || isRuleWord(word)) {
							continue;
						}
						learn.addTerm(new NewWord(word, Nature.NW));
					}

					if (tempTermNatures != TermNatures.NW) {
						mc.add(temp + TAB + "末##末", CRF_WEIGHT);
					}
				}
				
				graph.walkPath(mc.get());

				// 数字发现
				if (graph.hasNum) {
					NumRecognition.recognition(graph.terms);
				}

				// 词性标注
				List<Term> result = getResult();

				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms, 2, forests).recognition();
				graph.rmLittlePath();
				graph.walkPathByScore();

				// 进行新词发现
				new NewWordRecognition(graph.terms, learn).recognition();
				graph.walkPathByScore();

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

	/**
	 * 判断新词识别出来的词是否可信
	 * 
	 * @param word
	 * @return
	 */
	public static boolean isRuleWord(String word) {
		char c = 0;
		for (int i = 0; i < word.length(); i++) {
			c = word.charAt(i);
			if (c < 256 || (c = WordAlert.CharCover(word.charAt(i))) > 0 && c != '·') {
				return true;
			}
		}
		return false;
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
