package org.ansj.app.summary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.app.summary.pojo.Summary;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.tire.SmartGetWord;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

/**
 * 自动摘要,同时返回关键词
 * 
 * @author ansj
 * 
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

		List<Term> parse = NlpAnalysis.parse(query);

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
	 * @param keyword
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

		for (Sentence sentence : sentences) {
			computeScore(sentence, sf);
		}

		double maxScore = 0;
		int maxIndex = 0;

		for (int i = 0; i < sentences.size(); i++) {
			double tempScore = sentences.get(i).score;
			int tempLength = sentences.get(i).value.length();

			if (tempLength >= len) {
				if (maxScore < tempScore) {
					maxScore = tempScore;
					maxIndex = i;
					continue;
				}
			}
			for (int j = i + 1; j < sentences.size(); j++) {
				tempScore += sentences.get(j).score;
				tempLength += sentences.get(j).value.length();
				if (tempLength >= len) {
					if (maxScore < tempScore) {
						maxScore = tempScore;
						maxIndex = i;
						break;
					}
				}
			}

			if (tempLength < len) {
				if (maxScore < tempScore) {
					maxScore = tempScore;
					maxIndex = i;
					break;
				}
			} else {
				break;
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
			double value = len;

			StringBuilder newSummary = new StringBuilder();
			char c = 0;
			for (int i = 0; i < sb.length(); i++) {
				c = sb.charAt(i);
				if (c < 256) {
					value -= 0.5;
				} else {
					value -= 1;
				}

				if (value < 0) {
					break;
				}

				newSummary.append(c);
			}

			summaryStr = newSummary.toString();
		}

		return new Summary(keywords, summaryStr);
	}

	/**
	 * 计算一个句子的分数
	 * 
	 * @param sentence
	 * @param sf
	 */
	private void computeScore(Sentence sentence, SmartForest<Double> forest) {
		SmartGetWord<Double> sgw = new SmartGetWord<Double>(forest, sentence.value);
		while (sgw.getFrontWords() != null) {
			sentence.score += sgw.getParam();
		}
		if (sentence.score == 0) {
			sentence.score = sentence.value.length() * -0.005;
		} else {
			sentence.score /= Math.log(sentence.value.length() + 3);
		}
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
				if (i < chars.length - 1 && chars[i + 1] > 128) {
					insertIntoList(sb, sentences);
					sb = new StringBuilder();
				}
				break;
			case ' ':
			case '	':
			case ' ':
			case '。':
				insertIntoList(sb, sentences);
				sb = new StringBuilder();
				break;
			case ';':
			case '；':
				insertIntoList(sb, sentences);
				sb = new StringBuilder();
				break;
			case '!':
			case '！':
				insertIntoList(sb, sentences);
				sb = new StringBuilder();
				break;
			case '?':
			case '？':
				insertIntoList(sb, sentences);
				sb = new StringBuilder();
				break;
			case '\n':
			case '\r':
				insertIntoList(sb, sentences);
				sb = new StringBuilder();
				break;
			}
		}

		if (sb.length() > 0) {
			insertIntoList(sb, sentences);
		}

		return sentences;
	}

	private void insertIntoList(StringBuilder sb, List<Sentence> sentences) {
		String content = sb.toString().trim();
		if (content.length() > 0) {
			sentences.add(new Sentence(content));
		}
	}

	/*
	 * 句子对象
	 */
	class Sentence {
		String value;
		double score;

		public Sentence(String value) {
			this.value = value.trim();
		}

		public String toString() {
			return value;
		}
	}

}
