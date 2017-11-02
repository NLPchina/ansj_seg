package org.ansj.lucene.util;

import org.ansj.lucene6.AnsjAnalyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.util.Map;

public class AnsjTokenizerFactory extends TokenizerFactory {

	public final Log logger = LogFactory.getLog();

	private Map<String, String> args;

	public AnsjTokenizerFactory(Map<String, String> args) {
		super(args);
		this.args = args ;
	}

	@Override
	public Tokenizer create(AttributeFactory factory) {
		return AnsjAnalyzer.getTokenizer(null, args);
	}

}
