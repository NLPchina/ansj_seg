package org.ansj.library;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.ansj.dic.PathToStream;
import org.ansj.domain.KV;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;

public class SynonymsLibrary {

	private static final Log LOG = MyStaticValue.getLog(SynonymsLibrary.class);

	// 同义词典
	private static final Map<String, KV<String, SmartForest<List<String>>>> SYNONYMS = new HashMap<>();

	public static final String DEFAULT = "synonyms";

	static {
		for (Entry<String, String> entry : MyStaticValue.ENV.entrySet()) {
			if (entry.getKey().startsWith(DEFAULT)) {
				put(entry.getKey(), entry.getValue());
			}
		}
		putIfAbsent(DEFAULT, "library/synonyms.dic");
	}

	public static SmartForest<List<String>> get() {
		return get(DEFAULT);
	}

	/**
	 */
	public static SmartForest<List<String>> get(String key) {
		KV<String, SmartForest<List<String>>> kv = SYNONYMS.get(key);

		if (kv == null) {
			if (MyStaticValue.ENV.containsKey(key)) {
				putIfAbsent(key, MyStaticValue.ENV.get(key));
				return get(key);
			}
			LOG.warn("crf " + key + " not found in config ");
			return null;
		}

		SmartForest<List<String>> sw = (SmartForest<List<String>>) kv.getV();
		if (sw == null) {
			sw = init(key, kv);
		}
		return sw;
	}

	/**
	 * 加载
	 * 
	 * @return
	 */
	private static synchronized SmartForest<List<String>> init(String key, KV<String, SmartForest<List<String>>> kv) {

		SmartForest<List<String>> forest = kv.getV();
		if (forest != null) {
			return forest;
		}

		forest = new SmartForest<>();

		LOG.debug("begin init synonyms " + kv.getK());
		long start = System.currentTimeMillis();

		try (BufferedReader reader = IOUtil.getReader(PathToStream.stream(kv.getK()), IOUtil.UTF8)) {
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				}
				String[] split = temp.split("\t");

				List<String> list = new ArrayList<>();
				for (String word : split) {
					if (StringUtil.isBlank(word)) {
						continue;
					}
					list.add(word);
				}

				if (split.length <= 1) {
					LOG.warn(temp + " in synonymsLibrary not in to library !");
					continue;
				}

				for (int i = 0; i < split.length; i++) {
					forest.add(split[i], list);
				}
			}
			kv.setV(forest);
			LOG.info("load synonyms use time:" + (System.currentTimeMillis() - start) + " path is : " + kv.getK());
			return forest;
		} catch (Exception e) {
			LOG.error("Init synonyms library error :" + e.getMessage() + ", path: " + kv.getK());
			SYNONYMS.remove(key);
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

	public static void put(String key, String path, SmartForest<List<String>> value) {
		SYNONYMS.put(key, KV.with(path, value));
	}

	/**
	 * 删除一个key
	 * 
	 * @param key
	 * @return
	 */
	public static KV<String, SmartForest<List<String>>> remove(String key) {

		return SYNONYMS.remove(key);
	}

	/**
	 * 刷新一个,将值设置为null
	 * 
	 * @param key
	 * @return
	 */
	public static void reload(String key) {
		KV<String, SmartForest<List<String>>> kv = SYNONYMS.get(key);
		if (kv != null) {
			SYNONYMS.get(key).setV(null);
		}
	}

	public static Set<String> keys() {
		return SYNONYMS.keySet();
	}

	public static void putIfAbsent(String key, String path) {
		if (!SYNONYMS.containsKey(key)) {
			SYNONYMS.put(key, KV.with(path, (SmartForest<List<String>>) null));
		}
	}

	/**
	 * 覆盖更新同义词 [中国, 中华, 我国] -> replace([中国,华夏]) -> [中国,华夏]
	 * 
	 * @param words
	 */
	public static void insert(String key, String[] words) {
		SmartForest<List<String>> synonyms = get(key);

		List<String> list = new ArrayList<>();

		for (String word : words) {
			if (StringUtil.isBlank(word)) {
				continue;
			}
			list.add(word);
		}

		if (list.size() <= 1) {
			LOG.warn(Arrays.toString(words) + " not have any change because it less than 2 word");
			return;
		}

		Set<String> set = findAllWords(key, words);

		for (String word : list) {
			set.remove(word);
			synonyms.add(word, list);
		}

		for (String word : set) { //删除所有
			synonyms.remove(word);
			synonyms.getBranch(word).setParam(null);
		}

	}

	private static Set<String> findAllWords(String key, String[] words) {

		SmartForest<List<String>> synonyms = get(key);

		Set<String> set = new HashSet<>();
		for (String word : words) {
			SmartForest<List<String>> branch = synonyms.getBranch(word);
			if (branch != null) {
				List<String> params = branch.getParam();
				if (params != null) {
					set.addAll(params);
				}
			}
		}
		return set;
	}

	/**
	 * 合并更新同义词 覆盖更新同义词 [中国, 中华, 我国] -> append([中国,华夏]) -> [中国, 中华, 我国 , 华夏]
	 * 
	 * @param words
	 */
	public static void append(String key, String[] words) {

		SmartForest<List<String>> synonyms = get(key);

		Set<String> set = new HashSet<>();

		for (String word : words) {
			if (StringUtil.isBlank(word)) {
				continue;
			}
			set.add(word);
		}

		if (set.size() <= 1) {
			LOG.warn(Arrays.toString(words) + " not have any change because it less than 2 word");
			return;
		}

		set.addAll(findAllWords(key, words));

		List<String> list = new ArrayList<>(set);

		for (String word : list) {
			synonyms.addBranch(word, list);
		}
	}

	/**
	 * 从同义词组中删除掉一个词 [中国, 中华, 我国] -> remove(我国) -> [中国, 中华]
	 * 
	 * @param words
	 */
	public static void remove(String key, String word) {

		SmartForest<List<String>> synonyms = get(key);

		SmartForest<List<String>> branch = synonyms.getBranch(word);

		if (branch == null || branch.getStatus() < 2) {
			return;
		}

		List<String> params = branch.getParam();

		synonyms.remove(word);
		branch.setParam(null);
		params.remove(word);

		if (params.size() == 1) { //如果是1 个也删除
			synonyms.remove(params.get(0));
			params.remove(0);
		} else {
			params.remove(word);
		}
	}
}
