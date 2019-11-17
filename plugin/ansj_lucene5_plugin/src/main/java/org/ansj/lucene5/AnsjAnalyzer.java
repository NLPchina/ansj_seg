package org.ansj.lucene5;

import org.ansj.library.*;
import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.recognition.impl.SynonymsRecgnition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnsjAnalyzer extends Analyzer {
	public static final Log LOG = LogFactory.getLog();

	/**
	 * dic equals user , query equals to
	 * 
	 * @author ansj
	 *
	 */
	public static enum TYPE {
		base_ansj, index_ansj, query_ansj, dic_ansj, nlp_ansj
	}

	/**
	 * 分词类型
	 */
	private Map<String, String> args;

	/**
	 */
	public AnsjAnalyzer(Map<String, String> args) {
		this.args = args;
	}

	public AnsjAnalyzer(TYPE type, String dics) {
		this.args = new HashMap<String, String>();
		args.put("type", type.name());
		args.put(DicLibrary.DEFAULT, dics);
	}

	public AnsjAnalyzer(TYPE type) {
		this.args = new HashMap<String, String>();
		args.put("type", type.name());
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
	 * @return
	 */
	public static Tokenizer getTokenizer(Reader reader, Map<String, String> args) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("to create tokenizer " + args);
		}
		Analysis analysis = null;

		String temp = null;
		String type = args.get("type");

		if (type == null) {
			type = AnsjAnalyzer.TYPE.base_ansj.name();
		}

		switch (AnsjAnalyzer.TYPE.valueOf(type)) {
		case base_ansj:
			analysis = new BaseAnalysis();
			break;
		case index_ansj:
			analysis = new IndexAnalysis();
			break;
		case dic_ansj:
			analysis = new DicAnalysis();
			break;
		case query_ansj:
			analysis = new ToAnalysis();
			break;
		case nlp_ansj:
			analysis = new NlpAnalysis();
			if (StringUtil.isNotBlank(temp = args.get(CrfLibrary.DEFAULT))) {
				((NlpAnalysis) analysis).setCrfModel(CrfLibrary.get(temp));
			}
			break;
		default:
			analysis = new BaseAnalysis();
		}

		if (reader != null) {
			analysis.resetContent(reader);
		}

		if (StringUtil.isNotBlank(temp = args.get(DicLibrary.DEFAULT))) { //用户自定义词典
			String[] split = temp.split(",");
			Forest[] forests = new Forest[split.length];
			for (int i = 0; i < forests.length; i++) {
				if (StringUtil.isBlank(split[i])) {
					continue;
				}
				forests[i] = DicLibrary.get(split[i]);
			}
			analysis.setForests(forests);
		}

		List<StopRecognition> filters = null;
		if (StringUtil.isNotBlank(temp = args.get(StopLibrary.DEFAULT))) { //用户自定义词典
			String[] split = temp.split(",");
			filters = new ArrayList<StopRecognition>();
			for (String key : split) {
				StopRecognition stop = StopLibrary.get(key.trim());
				if (stop != null)
					filters.add(stop);
			}
		}

		List<SynonymsRecgnition> synonyms = null;
		if (StringUtil.isNotBlank(temp = args.get(SynonymsLibrary.DEFAULT))) { //同义词词典
			String[] split = temp.split(",");
			synonyms = new ArrayList<SynonymsRecgnition>();
			for (String key : split) {
				SmartForest<List<String>> sf = SynonymsLibrary.get(key.trim());
				if (sf != null)
					synonyms.add(new SynonymsRecgnition(sf));
			}
		}

		if (StringUtil.isNotBlank(temp = args.get(AmbiguityLibrary.DEFAULT))) { //歧义词典
			analysis.setAmbiguityForest(AmbiguityLibrary.get(temp.trim()));
		}

		if (StringUtil.isNotBlank(temp = args.get("isNameRecognition"))) { // 是否开启人名识别
			analysis.setIsNameRecognition(Boolean.valueOf(temp));
		}

		if (StringUtil.isNotBlank(temp = args.get("isNumRecognition"))) { // 是否开启数字识别
			analysis.setIsNumRecognition(Boolean.valueOf(temp));
		}

		if (StringUtil.isNotBlank(temp = args.get("isQuantifierRecognition"))) { //量词识别
			analysis.setIsQuantifierRecognition(Boolean.valueOf(temp));
		}

		if (StringUtil.isNotBlank(temp = args.get("isRealName"))) { //是否保留原字符
			analysis.setIsRealName(Boolean.parseBoolean(temp));
		}

		return new AnsjTokenizer(analysis, filters, synonyms);

	}

}