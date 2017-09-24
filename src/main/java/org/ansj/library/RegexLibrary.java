//package org.ansj.library;
//
//import org.ansj.dic.PathToStream;
//import org.ansj.domain.KV;
//import org.ansj.util.MyStaticValue;
//import org.nlpcn.commons.lang.tire.domain.Forest;
//import org.nlpcn.commons.lang.tire.domain.Value;
//import org.nlpcn.commons.lang.tire.library.Library;
//import org.nlpcn.commons.lang.util.IOUtil;
//import org.nlpcn.commons.lang.util.StringUtil;
//import org.nlpcn.commons.lang.util.logging.Log;
//import org.nlpcn.commons.lang.util.logging.LogFactory;
//
//import java.io.BufferedReader;
//import java.util.*;
//
//public class RegexLibrary {
//
//	private static final Log LOG = LogFactory.getLog();
//
//	public static final String DEFAULT = "regex";
//
//	public static final String DEFAULT_NATURE = "regex";
//
//	public static final Integer DEFAULT_FREQ = 1000;
//
//	public static final String DEFAULT_FREQ_STR = "1000";
//
//	// 正则表达式词典
//	private static final Map<String, KV<String, List<String>>> REGEX = new HashMap<>();
//
//	static {
//		for (Map.Entry<String, String> entry : MyStaticValue.ENV.entrySet()) {
//			if (entry.getKey().startsWith(DEFAULT)) {
//				put(entry.getKey(), entry.getValue());
//			}
//		}
//		putIfAbsent(DEFAULT, "library/regex.dic");
//
//		Forest forest = get();
//		if (forest == null) {
//			put(DEFAULT, DEFAULT, new ArrayList<String>());
//		}
//
//	}
//
//	/**
//	 * 关键词增加
//	 *
//	 * @param keyword 所要增加的关键词
//	 * @param nature 关键词的词性
//	 * @param freq 关键词的词频
//	 */
//	public static void insert(String key, String line, String nature, int freq) {
//		List<String> list = get(key);
//		String[] paramers = new String[2];
//		paramers[0] = nature;
//		paramers[1] = String.valueOf(freq);
//		Value value = new Value(keyword, paramers);
//		Library.insertWord(list, value);
//	}
//
//	/**
//	 * 增加关键词
//	 *
//	 * @param keyword
//	 */
//	public static void insert(String key, String keyword) {
//
//		insert(key, keyword, DEFAULT_NATURE, DEFAULT_FREQ);
//	}
//
//	/**
//	 * 删除关键词
//	 */
//	public static void delete(String key, String word) {
//
//		Forest dic = get(key);
//		if (dic != null) {
//			Library.removeWord(dic, word);
//		}
//	}
//
//	/**
//	 * 将用户自定义词典清空
//	 */
//	public static void clear(String key) {
//		get(key).clear();
//	}
//
//	public static Forest get() {
//		if (!DIC.containsKey(DEFAULT)) {
//			return null;
//		}
//		return get(DEFAULT);
//	}
//
//	/**
//	 * 根据模型名称获取crf模型
//	 *
//	 * @param modelName
//	 * @return
//	 */
//	public static List<String> get(String key) {
//
//		KV<String, List<String>> kv = REGEX.get(key);
//
//		if (kv == null) {
//			if (MyStaticValue.ENV.containsKey(key)) {
//				putIfAbsent(key, MyStaticValue.ENV.get(key));
//				return get(key);
//			}
//			LOG.warn("dic " + key + " not found in config ");
//			return null;
//		}
//		List<String> list = kv.getV();
//		if (list == null) {
//			list = init(key, kv, false);
//		}
//		return list;
//
//	}
//
//	/**
//	 * 根据keys获取词典集合
//	 *
//	 * @param keys
//	 * @return
//	 */
//	public static Forest[] gets(String... keys) {
//		Forest[] forests = new Forest[keys.length];
//		for (int i = 0; i < forests.length; i++) {
//			forests[i] = get(keys[i]);
//		}
//		return forests;
//	}
//
//	/**
//	 * 根据keys获取词典集合
//	 *
//	 * @param keys
//	 * @return
//	 */
//	public static Forest[] gets(Collection<String> keys) {
//		return gets(keys.toArray(new String[keys.size()]));
//	}
//
//	/**
//	 * 用户自定义词典加载
//	 *
//	 * @param key
//	 * @return
//	 */
//
//	private synchronized static List<String> init(String key, KV<String, List<String>> kv, boolean reload) {
//		List<String> list = kv.getV();
//		if (list != null) {
//			if (reload) {
//				list.clear();
//			} else {
//				return list;
//			}
//		} else {
//			list = new ArrayList<>();
//		}
//		try {
//
//			LOG.debug("begin init regex !");
//			long start = System.currentTimeMillis();
//			String temp = null;
//			String[] strs = null;
//			Value value = null;
//			try (BufferedReader br = IOUtil.getReader(PathToStream.stream(kv.getK()), "UTF-8")) {
//				list = IOUtil.readFile2List() ;
//			}
//			LOG.info("load regex use time:" + (System.currentTimeMillis() - start) + " path is : " + kv.getK());
//			kv.setV(list);
//			return list;
//		} catch (Exception e) {
//			LOG.error("Init regex library error :" + e.getMessage() + ", path: " + kv.getK());
//			REGEX.remove(key);
//			return null;
//		}
//	}
//
//	/**
//	 * 动态添加词典
//	 *
//	 */
//	public static void put(String key, String path, List<String> list) {
//		REGEX.put(key, KV.with(path, list));
//		MyStaticValue.ENV.put(key, path);
//	}
//
//	/**
//	 * 动态添加词典
//	 *
//	 */
//	public static void putIfAbsent(String key, String path) {
//
//		if (!REGEX.containsKey(key)) {
//			REGEX.put(key, KV.with(path, (List<String>) null));
//		}
//	}
//
//	/**
//	 * 动态添加词典
//	 *
//	 */
//	public static void put(String key, String path) {
//		put(key, path, null);
//	}
//
//	/**
//	 * 动态添加词典
//	 *
//	 * @param <T>
//	 * @param <T>
//	 *
//	 */
//	public static synchronized List<String> putIfAbsent(String key, String path, List<String> list) {
//		KV<String, List<String>> kv = REGEX.get(key);
//		if (kv != null && kv.getV() != null) {
//			return kv.getV();
//		}
//		put(key, path, list);
//		return list;
//	}
//
//	public static KV<String, List<String>> remove(String key) {
//		KV<String, List<String>> kv = REGEX.get(key);
//		if (kv != null && kv.getV() != null) {
//			kv.getV().clear();
//		}
//		MyStaticValue.ENV.remove(key) ;
//		return REGEX.remove(key);
//	}
//
//	public static Set<String> keys() {
//		return REGEX.keySet();
//	}
//
//	public static void reload(String key) {
//		if (!MyStaticValue.ENV.containsKey(key)) { //如果变量中不存在直接删掉这个key不解释了
//			remove(key);
//		}
//
//		putIfAbsent(key, MyStaticValue.ENV.get(key));
//
//		KV<String, List<String>> kv = REGEX.get(key);
//
//		init(key, kv, true);
//	}
//}
