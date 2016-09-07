package org.ansj.lucene.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.nlpcn.commons.lang.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnsjTokenizerFactory extends TokenizerFactory {

	public final Logger logger = LoggerFactory.getLogger(getClass());

	private String stopwordsDir;
	public Set<String> filter;
	private String type;

	public AnsjTokenizerFactory(Map<String, String> args) {
		super(args);
		stopwordsDir = get(args, "words");
		type = get(args, "type");
		addStopwords(stopwordsDir);
	}

	/**
	 * 添加停用词
	 * 
	 * @param dir
	 */
	private void addStopwords(String dir) {
		if (dir == null) {
			logger.info("no stopwords dir");
			return;
		}
		logger.info("stopwords: {}", dir);
		filter = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = IOUtil.getReader(dir, "uf-8");
			String word = br.readLine();
			while (word != null) {
				filter.add(word);
				word = br.readLine();
			}
		} catch (FileNotFoundException e) {
			logger.info("No stopword file found");
		} catch (IOException e) {
			logger.info("stopword file io exception");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Tokenizer create(AttributeFactory factory) {
		return AnsjAnalyzer.getTokenizer(null, AnsjAnalyzer.TYPE.valueOf(type), filter);
	}
	
}
