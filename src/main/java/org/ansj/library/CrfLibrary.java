package org.ansj.library;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.app.crf.model.CRFModel;
import org.ansj.dic.PathToStream;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;
import org.nlpcn.commons.lang.util.tuples.KeyValue;

public class CrfLibrary {

	private static final Log LOG = LogFactory.getLog();

	// CRF模型
	private static final Map<String, KeyValue<String, SplitWord>> CRF = new HashMap<>();

	public static final String DEFAULT = "crf_";

	/**
	 * 根据key获取crf分词器
	 * 
	 * @param key
	 * @return crf分词器
	 */
	public static SplitWord crf(String key) {
		KeyValue<String, SplitWord> kv = CRF.get(fix(key));

		if (kv == null) {
			LOG.warn("crf " + key + " not found in config ");
			return null;
		}

		SplitWord sw = (SplitWord) kv.getValue();
		if (sw == null) {
			sw = initCRFModel(kv);
		}
		return sw;
	}

	/**
	 * 加载CRF模型
	 * 
	 * @param modelPath
	 * @return
	 */
	private static synchronized SplitWord initCRFModel(KeyValue<String, SplitWord> kv) {
		try {
			if (kv.getValue() != null) {
				return kv.getValue();
			}

			long start = System.currentTimeMillis();
			LOG.info("begin init crf model!");
			try (InputStream is = PathToStream.stream(kv.getKey())) {
				SplitWord crfSplitWord = new SplitWord(Model.load(CRFModel.class, is));
				kv.setValue(crfSplitWord);
				LOG.info("load crf use time:" + (System.currentTimeMillis() - start) + " path is : " + kv.getKey());
				return crfSplitWord;
			}
		} catch (Exception e) {
			LOG.error(kv + " load err " + e.getMessage());
			return null;
		}
	}

	/**
	 * 动态添加
	 * 
	 * @param dicDefault
	 * @param dicDefault2
	 * @param dic2
	 */
	public static void put(String key, String path) {
		put(key, path, null);
	}

	public static void put(String key, String path, SplitWord sw) {
		CRF.put(key, KeyValue.with(path, sw));
	}

	/**
	 * 删除一个key
	 * 
	 * @param key
	 * @return
	 */
	public static KeyValue<String, SplitWord> remove(String key) {
		return CRF.remove(key);
	}

	/**
	 * 刷新一个,将值设置为null
	 * @param key
	 * @return
	 */
	public static KeyValue<String, SplitWord> flush(String key) {
		CRF.get(key).setValue(null);
	}

	public static Set<String> keys() {
		return CRF.keySet();
	}

	private static String fix(String key) {
		if (key.startsWith(DEFAULT)) {
			return key;
		} else {
			return DEFAULT + key;
		}
	}
}
