package org.ansj.app.summary;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.app.summary.pojo.Summary;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.tire.SmartGetWord;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.MapCount;
import org.nlpcn.commons.lang.util.WordAlert;
import org.nlpcn.commons.lang.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自动摘要,同时返回关键词
 *
 * @author ansj
 */
public class SummaryComputer {

	private static final Set<String> FILTER_SET = new HashSet<String>();

	static {
		FILTER_SET.add("w");
		FILTER_SET.add("null");
	}

	/**
	 * summaryLength
	 */
	private int len = 300;

	private boolean isSplitSummary = true;

	String title, content;

	public SummaryComputer(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public SummaryComputer(int len, String title, String content) {
		this.len = len;
		this.title = title;
		this.content = content;
	}

	public SummaryComputer(int len, boolean isSplitSummary, String title, String content) {
		this.len = len;
		this.title = title;
		this.content = content;
		this.isSplitSummary = isSplitSummary;
	}

	/**
	 * 计算摘要，利用关键词抽取计算
	 *
	 * @return
	 */
	public Summary toSummary() {
		return toSummary(new ArrayList<Keyword>());
	}

	/**
	 * 根据用户查询串计算摘要
	 *
	 * @return
	 */
	public Summary toSummary(String query) {

		List<Term> parse = NlpAnalysis.parse(query).getTerms();

		List<Keyword> keywords = new ArrayList<Keyword>();
		for (Term term : parse) {
			if (FILTER_SET.contains(term.natrue().natureStr)) {
				continue;
			}
			keywords.add(new Keyword(term.getName(), term.termNatures().allFreq, 1));
		}

		return toSummary(keywords);
	}

	/**
	 * 计算摘要，传入用户自己算好的关键词
	 *
	 * @return
	 */
	public Summary toSummary(List<Keyword> keywords) {

		if (keywords == null) {
			keywords = new ArrayList<Keyword>();
		}

		if (keywords.size() == 0) {

			KeyWordComputer kc = new KeyWordComputer(10);
			keywords = kc.computeArticleTfidf(title, content);
		}
		return explan(keywords, content);
	}

	/**
	 * 计算摘要
	 *
	 * @param keywords
	 * @param content
	 * @return
	 */
	private Summary explan(List<Keyword> keywords, String content) {

		SmartForest<Double> sf = new SmartForest<Double>();

		for (Keyword keyword : keywords) {
			sf.add(keyword.getName(), keyword.getScore());
		}


		// 先断句
		List<Sentence> sentences = toSentenceList(content.toCharArray());

		boolean flag = false;

		for (Sentence sentence : sentences) {
			flag = computeScore(sentence, sf, false) || flag ;
		}

		if (!flag) {
			if (content.length() <= len) {
				return new Summary(keywords, content);
			}
			return new Summary(keywords, content.substring(0, len));
		}

		double maxScore = 0;
		int maxIndex = 0;

		MapCount<String> mc = new MapCount<>();

		for (int i = 0; i < sentences.size(); i++) {
			double tempScore = sentences.get(i).score;
			int tempLength = sentences.get(i).value.length();
			mc.addAll(sentences.get(i).mc.get());

			if (tempLength >= len) {
				tempScore = tempScore * mc.get().size();
				if (maxScore <= tempScore) {
					maxScore = tempScore;
					maxIndex = i;
				} else {
					mc.get().clear();
				}
				continue;
			}
			for (int j = i + 1; j < sentences.size(); j++) {
				tempScore += sentences.get(j).score;
				tempLength += sentences.get(j).value.length();
				mc.addAll(sentences.get(j).mc.get());

				if (tempLength >= len) {
					tempScore = tempScore * mc.get().size();
					if (maxScore <= tempScore) {
						maxScore = tempScore;
						maxIndex = i;
					}
					mc.get().clear();
					break;
				}
			}

			if (tempLength < len) {
				tempScore = tempScore * mc.get().size();
				if (maxScore <= tempScore) {
					maxScore = tempScore;
					maxIndex = i;
					break;
				}
				mc.get().clear();
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = maxIndex; i < sentences.size(); i++) {
			sb.append(sentences.get(i).value);
			if (sb.length() > len) {
				break;
			}
		}

		String summaryStr = sb.toString();

		/**
		 * 是否强制文本长度。对于abc这种字符算半个长度
		 */

		if (isSplitSummary && sb.length() > len) {

			String str = sb.toString();
			Sentence sentence = new Sentence(str);

			computeScore(sentence, sf, true);

			List<Triplet<Integer, Integer, Double>> offset = sentence.offset;

			List<Integer> beginArr = new ArrayList<>() ;

			f: for (int i = 0; i < str.length(); i++) {
				for (Triplet<Integer,Integer,Double> t : offset) {
					if(i>t.getValue0() && i<t.getValue1()){
						continue f;
					}
				}

				if(str.length()-i < len){
					break  ;
				}

				beginArr.add(i);
			}

			maxIndex = 0 ;
			maxScore = -10000 ;



			for (Integer begin : beginArr) {
				double score = 0 ;
				for (Triplet<Integer,Integer,Double> t : offset) {
					if(begin<t.getValue0() && begin+len>t.getValue1()){
						score += t.getValue2() ;
					}
				}
				if(score>maxScore){
					maxIndex = begin ;
					maxScore = score ;
				}
			}

			summaryStr = str.substring(maxIndex, Math.min(maxIndex + len + 1, str.length()));
		}


		return new Summary(keywords, summaryStr);

	}

	/**
	 * 计算一个句子的分数
	 *
	 * @param sentence
	 */
	private boolean computeScore(Sentence sentence, SmartForest<Double> forest, boolean offset) {
		SmartGetWord<Double> sgw = new SmartGetWord<Double>(forest, sentence.value.toLowerCase());
		String name = null;
		boolean flag = false;
		while ((name = sgw.getFrontWords()) != null) {
			flag = true;
			sentence.updateScore(name, sgw.getParam());
			if (offset) {
				Triplet<Integer, Integer, Double> triplet = new Triplet<Integer, Integer, Double>(sgw.offe, sgw.offe+name.length(), sgw.getParam());
				sentence.offset.add(triplet);
			}
		}
		if (sentence.score == 0) {
			sentence.score = sentence.value.length() * -0.005;
		} else {
			sentence.score /= Math.log(sentence.value.length() + 3);
		}

		return flag;
	}

	public List<Sentence> toSentenceList(char[] chars) {

		StringBuilder sb = new StringBuilder();

		List<Sentence> sentences = new ArrayList<Sentence>();

		for (int i = 0; i < chars.length; i++) {
			if (sb.length() == 0 && (Character.isWhitespace(chars[i]) || chars[i] == ' ')) {
				continue;
			}

			sb.append(chars[i]);
			switch (chars[i]) {
				case '.':
					if (i < chars.length - 1 && !WordAlert.isNumber(chars[i + 1])) {
						insertIntoList(sb, sentences,false);
						sb = new StringBuilder();
					}
					break;
				//			case ' ':
				case '	':
				case '　':
				case ' ':
				case ',':
				case '。':
				case ';':
				case '；':
				case '!':
				case '！':
				case '，':
				case '?':
				case '？':
					insertIntoList(sb, sentences,false);
					sb = new StringBuilder();
					break;

				case '\n':
				case '\r':
					insertIntoList(sb, sentences,true);
					sb = new StringBuilder();
					break;

			}
		}

		if (sb.length() > 0) {
			insertIntoList(sb, sentences,false);
		}

		return sentences;
	}

	private void insertIntoList(StringBuilder sb, List<Sentence> sentences , boolean space) {
		String content = sb.toString().trim();
		if (content.length() > 0) {
			sentences.add(new Sentence(content));
			if(space){
				sentences.add(new Sentence(" ")) ;
			}
		}
	}

	/*
	 * 句子对象
	 */
	public class Sentence {
		String value;
		private double score;

		List<Triplet<Integer, Integer, Double>> offset = new ArrayList<>();

		private MapCount<String> mc = new MapCount<>();

		public Sentence(String value) {
			this.value = value;
		}

		public void updateScore(String name, double score) {
			mc.add(name);
			Double size = mc.get().get(name);
			this.score += score / size;
		}

		@Override
		public String toString() {
			return value;
		}

	}

}
