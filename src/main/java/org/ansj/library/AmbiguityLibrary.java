package org.ansj.library;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ansj.dic.PathToStream;
import org.ansj.domain.KV;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

public class AmbiguityLibrary {

	private static final Log LOG = LogFactory.getLog();

	// 同义词典
	private static final Map<String, KV<String, Forest>> AMBIGUITY = new HashMap<>();

	public static final String DEFAULT = "ambiguity_";

	/**
	 * 获取系统默认词典
	 * 
	 * @return
	 */
	public static Forest get() {
		return get(DEFAULT);
	}

	/**
	 * 根据key获取
	 * 
	 * @param key
	 * @return crf分词器
	 */
	public static Forest get(String key) {
		key = fix(key);
		KV<String, Forest> kv = AMBIGUITY.get(key);

		if (kv == null) {
			LOG.warn("crf " + key + " not found in config ");
			return null;
		}

		Forest sw = (Forest) kv.getV();
		if (sw == null) {
			sw = init(kv);
		}
		return sw;
	}

	/**
	 * 加载
	 * 
	 * @return
	 */
	private static synchronized Forest init(KV<String, Forest> kv) {
		Forest forest = kv.getV();
		if (forest != null) {
			return forest;
		}
		forest = new Forest();
		try (BufferedReader br = IOUtil.getReader(PathToStream.stream(kv.getK()), "utf-8")) {
			String temp;
			LOG.info("begin init dic !");
			long start = System.currentTimeMillis();
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isNotBlank(temp)) {
					temp = StringUtil.trim(temp);
					String[] split = temp.split("\t");
					StringBuilder sb = new StringBuilder();
					if (split.length % 2 != 0) {
						LOG.error("init ambiguity  error in line :" + temp + " format err !");
						continue;
					}
					for (int i = 0; i < split.length; i += 2) {
						sb.append(split[i]);
					}
					forest.addBranch(sb.toString(), split);
				}
			}
			LOG.info("load dic use time:" + (System.currentTimeMillis() - start) + " path is : " + kv.getK());
			kv.setV(forest);
			return forest;
		} catch (Exception e) {
			LOG.error("Init ambiguity library error :" + e.getMessage() + ", path: " + kv.getK());
			return null;
		}
	}

	/**
	 * 插入到树中呀
	 * 
	 * @param key
	 * @param split
	 * @return
	 */
	public static void insert(String key, String... split) {
		Forest forest = get(key);
		StringBuilder sb = new StringBuilder();
		if (split.length % 2 != 0) {
			LOG.error("init ambiguity  error in line :" + Arrays.toString(split) + " format err !");
			return;
		}
		for (int i = 0; i < split.length; i += 2) {
			sb.append(split[i]);
		}
		forest.addBranch(sb.toString(), split);
	}

	/**
	 * 插入到树种
	 * @param key
	 * @param value
	 */
	public static void insert(String key, Value value) {
		Forest forest = get(key);
		Library.insertWord(forest, value);
	}

	/**
	 * 动态添加
	 * 
	 * @param dicDefault
	 * @param dicDefault2
	 * @param dic2
	 */
	public static void put(String key, String path) {
		key = fix(key);
		put(key, path, null);
	}

	public static void put(String key, String path, Forest value) {
		key = fix(key);
		AMBIGUITY.put(key, KV.with(path, value));
	}

	/**
	 * 删除一个key
	 * 
	 * @param key
	 * @return
	 */
	public KV<String, Forest> remove(String key) {
		key = fix(key);
		return AMBIGUITY.remove(key);
	}

	/**
	 * 刷新一个,将值设置为null
	 * 
	 * @param key
	 * @return
	 */
	public static void reload(String key) {
		key = fix(key);
		AMBIGUITY.get(key).setV(null);
		get(key);
	}

	public static Set<String> keys() {
		return AMBIGUITY.keySet();
	}

	private static String fix(String key) {
		if (key.startsWith(DEFAULT)) {
			return key;
		} else {
			return DEFAULT + key;
		}
	}

	public static void putIfAbsent(String key, String path) {
		if (!AMBIGUITY.containsKey(key)) {
			AMBIGUITY.put(key, KV.with(path, (Forest) null));
		}
	}

}
