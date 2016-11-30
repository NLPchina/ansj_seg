package org.ansj.lucene5;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

public class AnsjAnalyzer extends Analyzer {
	public final Log logger = LogFactory.getLog();

	/**
	 * dic equals user , query equals to
	 * 
	 * @author ansj
	 *
	 */
	public static enum TYPE {
		base, index, query, to, dic, user, search
	}

	/**
	 * 分词类型
	 */
	private Map<String, String> args;

	/**
	 * @param filter 停用词
	 */
	public AnsjAnalyzer(Map<String, String> args) {
		this.args = args;
	}

	@Override
	protected TokenStreamComponents createComponents(String text) {
		BufferedReader reader = new BufferedReader(new StringReader(text));
		Tokenizer tokenizer = null;
		tokenizer = getTokenizer(reader, this.args);
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
	public static Tokenizer getTokenizer(BufferedReader reader, Map<String, String> args) {

		Analysis analysis = null;

		switch (AnsjAnalyzer.TYPE.valueOf(args.get("type"))) {
		case base:
			analysis = new BaseAnalysis();
			break;
		case index:
			analysis = new IndexAnalysis();
			break;
		case dic:
		case user:
			analysis = new DicAnalysis();
			break;
		case to:
		case query:
		case search:
			analysis = new ToAnalysis();
			break;
		default:
			analysis = new BaseAnalysis();
		}

		if (reader != null) {
			analysis.resetContent(reader);
		}

		return new AnsjTokenizer(analysis);

	}

}