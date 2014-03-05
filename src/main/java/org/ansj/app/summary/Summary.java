package org.ansj.app.summary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import love.cq.domain.SmartForest;
import love.cq.splitWord.SmartGetWord;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

/**
 * 自动摘要,关键词标红
 * 
 * @author ansj
 * 
 */
public class Summary {

	private int size;

	private String beginTag, endTag;

	public Summary(int size) {
		this.size = size;
	}

	public Summary(String beginTag, String endTag, int size) {
		this.beginTag = beginTag;
		this.endTag = endTag;
		this.size = size;
	}

	/**
	 * 传入标题正文计算摘要
	 * 
	 * @param title
	 * @param content
	 * @return
	 */
	public String tagAndSummary(String queryOrTitle, String content) {
		return null;
	}

	/**
	 * 传入标题正文计算摘要
	 * 
	 * @param title
	 * @param content
	 * @return
	 */
	public String tagAndSummary(String content) {
		KeyWordComputer kc = new KeyWordComputer();
		Collection<Keyword> keyword = kc.computeArticleTfidf(content);
		return explan(keyword, content);
	}

	/**
	 * 计算摘要
	 * 
	 * @param keyword
	 * @param content
	 * @return
	 */
	private String explan(Collection<Keyword> keywords, String content) {

		SmartForest<Double> sf = new SmartForest<Double>();

		for (Keyword keyword : keywords) {
			sf.add(keyword.getName(), keyword.getScore());
		}
System.out.println(keywords);
		// 先断句
		List<Sentence> sentences = toSentenceList(content.toCharArray());

		for (Sentence sentence : sentences) {
			computeScore(sentence, sf);
		}

		for (Sentence sentence : sentences) {
			System.out.println(sentence.score + "\t" + sentence.value);
		}

		return null;
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
	}

	public List<Sentence> toSentenceList(char[] chars) {

		StringBuilder sb = new StringBuilder();

		List<Sentence> sentences = new ArrayList<Sentence>();

		for (int i = 0; i < chars.length; i++) {
			sb.append(chars[i]);
			switch (chars[i]) {
			case '.':
				if (i < chars.length - 1 && chars[i + 1] > 128) {
					sentences.add(new Sentence(sb.toString()));
					sb = new StringBuilder();
				}
				break;
			case ';':
				sentences.add(new Sentence(sb.toString()));
				sb = new StringBuilder();
				break;
			case '!':
				sentences.add(new Sentence(sb.toString()));
				sb = new StringBuilder();
				break;
			case '?':
				sentences.add(new Sentence(sb.toString()));
				sb = new StringBuilder();
				break;
			case '\n':
				sentences.add(new Sentence(sb.toString()));
				sb = new StringBuilder();
				break;
			}
		}

		return sentences;
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

	public static void main(String[] args) {

		String content = "中新社北京3月2日电 中国云南昆明“3·01”严重暴力恐怖事件发生后，一些国家和国际组织予以强烈谴责。\n  俄罗斯总统普京向国家主席习近平致慰问电，表示俄方对这种令人发指的犯罪行为予以坚决谴责，愿与中方就打击恐怖主义进一步全力开展合作。普京向遇难者亲属表示深切同情，希望所有伤者早日康复。\n  联合国秘书长潘基文发表声明，最强烈谴责这起针对平民的恐怖袭击，强调没有任何借口滥杀无辜平民，犯罪分子应被绳之以法。潘基文向死难者家属表示慰问，祝愿伤者早日康复。\n  法国外交部发表声明，对这起造成众多人员伤亡的流血袭击事件予以强烈谴责，强调任何理由都不能为此类行径辩护。法方向受害者家属表示慰问，将和中国政府和人民保持团结。\n美国驻华使馆和美国国务院分别向中国外交部和中国驻美使馆表示，美方对这起造成重大人员伤亡的严重暴力事件感到震惊。美方谴责这一残忍的暴力行径。美国政府向中方表示慰问，对死难者表示哀悼，向受害者及其家属表示同情。\n日本驻华使馆和日本外务省分别通过中国外交部和中国驻日使馆对这起暴恐事件遇难人员表示哀悼。\n多米尼克总理斯凯里特也对遇难人员表示哀悼。(完)";

		Summary summary = new Summary(300);

		String result = summary.tagAndSummary(content);
	}
}
