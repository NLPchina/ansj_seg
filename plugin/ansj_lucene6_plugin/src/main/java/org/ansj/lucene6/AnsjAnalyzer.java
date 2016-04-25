package org.ansj.lucene6;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

public class AnsjAnalyzer extends Analyzer {

	/**
	 * dic equals user , query equals to
	 * 
	 * @author ansj
	 *
	 */
	public static enum TYPE {
		index, query, to, dic, user, search
	}

	/** 自定义停用词 */
	private Set<String> filter;
	/** 是否查询分词 */
	private TYPE type;

	/**
	 * @param filter
	 *            停用词
	 * @param pstemming
	 *            是否分析词干
	 */
	public AnsjAnalyzer(TYPE type, Set<String> filter) {
		this.type = type;
		this.filter = filter;
	}

	public AnsjAnalyzer(TYPE type, String stopwordsDir) {
		this.type = type;
		this.filter = filter(stopwordsDir);
	}

	public AnsjAnalyzer(TYPE type) {
		this.type = type;
	}

	public AnsjAnalyzer() {
		this.type = TYPE.query;
	}

	private Set<String> filter(String stopwordsDir) {
		if (StringUtil.isBlank(stopwordsDir)) {
			return null;
		}
		try {
			List<String> readFile2List = IOUtil.readFile2List(stopwordsDir, IOUtil.UTF8);
			return new HashSet<String>(readFile2List);
		} catch (Exception e) {
			System.err.println("not foun stop word path by " + new File(stopwordsDir).getAbsolutePath());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected TokenStreamComponents createComponents(String text) {
		BufferedReader reader = new BufferedReader(new StringReader(text));
		Tokenizer tokenizer = null;

		tokenizer = getTokenizer(reader, this.type, this.filter);
		return new TokenStreamComponents(tokenizer);
	}

	/**
	 * 获得一个tokenizer
	 * 
	 * @param reader
	 * @param type
	 * @param filter
	 * @return
	 */
	public static Tokenizer getTokenizer(BufferedReader reader, TYPE type, Set<String> filter) {
		Tokenizer tokenizer;

		switch (type) {
		case index:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new IndexAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new IndexAnalysis(reader), filter);
			}
			break;
		case dic:
		case user:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new DicAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new DicAnalysis(reader), filter);
			}
			break;

		case to:
		case query:
		case search:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new ToAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new ToAnalysis(reader), filter);
			}
			break;
		default:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new ToAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new ToAnalysis(reader), filter);
			}
		}

		return tokenizer;
	}
}