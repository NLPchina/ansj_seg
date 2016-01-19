package org.ansj.lucene5;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.splitWord.analysis.UserDefineAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

public class AnsjAnalyzer extends Analyzer {
	/** 自定义停用词 */
	private Set<String> filter;
	/** 是否查询分词 */
	private String type;

	/**
	 * @param filter
	 *            停用词
	 * @param pstemming
	 *            是否分析词干
	 */
	public AnsjAnalyzer(String type, Set<String> filter) {
		this.type = type;
		this.filter = filter;
	}

	public AnsjAnalyzer(String type, String stopwordsDir) {
		this.type = type;
		this.filter = filter(stopwordsDir);
	}

	public AnsjAnalyzer(String type) {
		this.type = type;
	}
	
	public AnsjAnalyzer(){}

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

		if ("user".equalsIgnoreCase(type)) {
			tokenizer = new AnsjTokenizer(new UserDefineAnalysis(reader), filter);
		} else if ("index".equalsIgnoreCase(type)) {
			tokenizer = new AnsjTokenizer(new IndexAnalysis(reader), filter);
		} else {
			tokenizer = new AnsjTokenizer(new ToAnalysis(reader), filter);
		}
		return new TokenStreamComponents(tokenizer);
	}
}