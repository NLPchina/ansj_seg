package org.ansj.library;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ansj.dic.PathToStream;
import org.ansj.domain.KV;
import org.ansj.recognition.impl.FilterRecognition;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

public class FilterLibrary {

	private static final Log LOG = LogFactory.getLog();

	public static final String DEFAULT = "filter";

	// 用户自定义词典
	private static final Map<String, KV<String, FilterRecognition>> FILTER = new HashMap<>();

	/**
	 * 词性过滤
	 * 
	 * @param key
	 * @param filterNatures
	 */
	public static void insertStopNatures(String key, String... filterNatures) {
		FilterRecognition fr = get(key);
		fr.insertStopNatures(filterNatures);
	}

	/**
	 * 正则过滤
	 * 
	 * @param key
	 * @param regexes
	 */
	public static void insertStopRegexes(String key, String... regexes) {
		FilterRecognition fr = get(key);
		fr.insertStopRegexes(regexes);
	}

	/**
	 * 增加停用词
	 * 
	 * @param key
	 * @param regexes
	 */
	public static void insertStopWords(String key, String... stopWords) {
		FilterRecognition fr = get(key);
		fr.insertStopWords(stopWords);
	}

	/**
	 * 增加停用词
	 * 
	 * @param key
	 * @param regexes
	 */
	public static void insertStopWords(String key, List<String> stopWords) {
		FilterRecognition fr = get(key);
		fr.insertStopWords(stopWords);
	}

	public static FilterRecognition get() {
		return get(DEFAULT);
	}

	/**
	 * 根据模型名称获取crf模型
	 * 
	 * @param modelName
	 * @return
	 */
	public static FilterRecognition get(String key) {
		KV<String, FilterRecognition> kv = FILTER.get(key);

		if (kv == null) {
			LOG.warn("FILTER " + key + " not found in config ");
			return null;
		}
		FilterRecognition FilterRecognition = kv.getV();
		if (FilterRecognition == null) {
			FilterRecognition = init(key, kv);
		}
		return FilterRecognition;

	}

	/**
	 * 用户自定义词典加载
	 * 
	 * @param key
	 * @param path
	 * @return
	 */

	private synchronized static FilterRecognition init(String key, KV<String, FilterRecognition> kv) {
		FilterRecognition filterRecognition = kv.getV();
		if (filterRecognition != null) {
			return filterRecognition;
		}
		try {
			filterRecognition = new FilterRecognition();
			LOG.info("begin init FILTER !");
			long start = System.currentTimeMillis();
			String temp = null;
			String[] strs = null;
			try (BufferedReader br = IOUtil.getReader(PathToStream.stream(kv.getK()), "UTF-8")) {
				while ((temp = br.readLine()) != null) {
					if (StringUtil.isNotBlank(temp)) {
						temp = StringUtil.trim(temp);
						strs = temp.split("\t");

						if (strs.length == 1) {
							filterRecognition.insertStopWords(strs[0]);
						} else {
							switch (strs[1]) {
							case "nature":
								filterRecognition.insertStopNatures(strs[0]);
								break;
							case "regex":
								filterRecognition.insertStopRegexes(strs[0]);
								break;
							default:
								filterRecognition.insertStopWords(strs[0]);
								break;
							}
						}

					}
				}
			}
			LOG.info("load FILTER use time:" + (System.currentTimeMillis() - start) + " path is : " + kv.getK());
			kv.setV(filterRecognition);
			return filterRecognition;
		} catch (Exception e) {
			LOG.error("Init ambiguity library error :" + e.getMessage() + ", path: " + kv.getK());
			FILTER.remove(key);
			return null;
		}
	}

	/**
	 * 动态添加词典
	 * 
	 * @param FILTERDefault
	 * @param FILTERDefault2
	 * @param FILTER2
	 */
	public static void put(String key, String path, FilterRecognition filterRecognition) {
		KV<String, FilterRecognition> kv = FILTER.get(key);
		if (kv == null) {
			kv = KV.with(path, filterRecognition);
		} else {
			kv.setK(path);
			kv.setV(filterRecognition);
		}
		FILTER.put(key, kv);
	}

	/**
	 * 动态添加词典
	 * 
	 * @param FILTERDefault
	 * @param FILTERDefault2
	 * @param FILTER2
	 */
	public static void putIfAbsent(String key, String path) {
		if (!FILTER.containsKey(key)) {
			FILTER.put(key, KV.with(path, (FilterRecognition) null));
		}
	}

	/**
	 * 动态添加词典
	 * 
	 * @param FILTERDefault
	 * @param FILTERDefault2
	 * @param FILTER2
	 */
	public static void put(String key, String path) {
		put(key, path, null);
	}

	/**
	 * 动态添加词典
	 * 
	 * @param <T>
	 * @param <T>
	 * 
	 * @param FILTERDefault
	 * @param FILTERDefault2
	 * @param FILTER2
	 */
	public static synchronized FilterRecognition putIfAbsent(String key, String path, FilterRecognition FilterRecognition) {
		KV<String, FilterRecognition> kv = FILTER.get(key);
		if (kv != null && kv.getV() != null) {
			return kv.getV();
		}
		put(key, path, FilterRecognition);
		return FilterRecognition;
	}

	public static KV<String, FilterRecognition> remove(String key) {
		return FILTER.remove(key);
	}

	public static Set<String> keys() {
		return FILTER.keySet();
	}

	public static void reload(String key) {
		FILTER.get(key).setV(null);
		get(key);
	}

}
