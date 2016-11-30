package org.ansj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.app.crf.model.CRFModel;
import org.ansj.dic.DicReader;
import org.ansj.dic.PathToStream;
import org.ansj.domain.AnsjItem;
import org.ansj.exception.LibraryException;
import org.ansj.library.DATDictionary;
import org.ansj.library.DicLibrary;
import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.FileFinder;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

/**
 * 这个类储存一些公用变量.
 * 
 * @author ansj
 * 
 */
public class MyStaticValue {

	public static final Forest EMPTY_FOREST = new Forest();

	private static final Log LOG = LogFactory.getLog(MyStaticValue.class);


	public static final String AMBIGUITY_DEFAULT = "ambiguity_";

	public static final String SYNONYMS_DEFAULT = "synonyms_";

	// 是否开启人名识别
	public static Boolean isNameRecognition = true;

	// 是否开启数字识别
	public static Boolean isNumRecognition = true;

	// 是否数字和量词合并
	public static Boolean isQuantifierRecognition = true;

	// 是否显示真实词语
	public static Boolean isRealName = false;


	// 歧义词典
	public static final Map<String, String> AMBIGUITY = new HashMap<>();

	// 同义词典
	public static final Map<String, String> SYNONYMS = new HashMap<>();

	//存放所有的词典
	private static final Map<String, Object> ALL = new HashMap<>();

	//默认的词性
	public static final String DEFAULT_NATURE = "userDefine";

	//默认的词频
	public static final String DEFAULT_FREQ_STR = "1000";

	/**
	 * 是否用户辞典不加载相同的词
	 */
	public static boolean isSkipUserDefine = false;

	static {
		/**
		 * 配置文件变量
		 */
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("ansj_library");
		} catch (Exception e) {
			try {
				File find = FileFinder.find("ansj_library.properties", 1);
				if (find != null && find.isFile()) {
					rb = new PropertyResourceBundle(IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
					LOG.info("load ansj_library not find in classPath ! i find it in " + find.getAbsolutePath() + " make sure it is your config!");
				}
			} catch (Exception e1) {
				LOG.warn("not find ansj_library.properties. and err {} i think it is a bug!", e1);
			}
		}

		if (rb == null) {
			try {
				rb = ResourceBundle.getBundle("library");
			} catch (Exception e) {
				try {
					File find = FileFinder.find("library.properties", 2);
					if (find != null && find.isFile()) {
						rb = new PropertyResourceBundle(IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
						LOG.info("load library not find in classPath ! i find it in " + find.getAbsolutePath() + " make sure it is your config!");
					}
				} catch (Exception e1) {
					LOG.warn("not find library.properties. and err {} i think it is a bug!", e1);
				}
			}
		}

		if (rb == null) {
			LOG.warn("not find library.properties in classpath use it by default !");
		} else {

			for (String key : rb.keySet()) {

				if (key.equals("dic")) {
					DicLibrary.put(DicLibrary.DEFAULT, rb.getString(key));
				} else if (key.equals("crf")) {
					CRF.put(CRF_DEFAULT, rb.getString(key));
				} else if (key.equals("ambiguity")) {
					AMBIGUITY.put(AMBIGUITY_DEFAULT, rb.getString(key));
				} else if (key.equals("synonyms")) {
					SYNONYMS.put(AMBIGUITY_DEFAULT, rb.getString(key));
				} else if (key.startsWith("dic_")) {
					if (DicLibrary.DIC.containsKey(key)) {
						LOG.warn(key + " dic config repeat definition now overwrite it !");
					}
					DicLibrary.put(key, rb.getString(key));
				} else if (key.startsWith("crf_")) {
					if (CRF.containsKey(key)) {
						LOG.warn(key + " crf config repeat definition now overwrite it !");
					}
					CRF.put(key, rb.getString(key));
				} else if (key.startsWith("synonyms_")) {
					if (CRF.containsKey(key)) {
						LOG.warn(key + " crf config repeat definition now overwrite it !");
					}
					SYNONYMS.put(key, rb.getString(key));
				} else if (key.startsWith("ambiguity_")) {
					if (CRF.containsKey(key)) {
						LOG.warn(key + " crf config repeat definition now overwrite it !");
					}
					AMBIGUITY.put(key, rb.getString(key));
				} else {
					try {
						Field field = MyStaticValue.class.getField(key);
						field.set(null, ObjConver.conversion(rb.getString(key), field.getType()));
					} catch (NoSuchFieldException e) {
						LOG.error("not find field by " + key);
					} catch (SecurityException e) {
						LOG.error("安全异常", e);
					} catch (IllegalArgumentException e) {
						LOG.error("非法参数", e);
					} catch (IllegalAccessException e) {
						LOG.error("非法访问", e);
					}
				}

			}

		}

		//如果没有设置则设置默认路径
		DicLibrary.putIfAbsent(DicLibrary.DEFAULT, "library/default.dic");

		CRF.putIfAbsent(CRF_DEFAULT, "jar://crf.model");

		AMBIGUITY.putIfAbsent(DIC_DEFAULT, "library/ambiguity.dic");

		SYNONYMS.putIfAbsent(DIC_DEFAULT, "library/synonyms.dic");
	}

	/**
	 * 人名词典
	 * 
	 * @return
	 */
	public static BufferedReader getPersonReader() {
		return DicReader.getReader("person/person.dic");
	}

	/**
	 * 机构名词典
	 * 
	 * @return
	 */
	public static BufferedReader getCompanReader() {
		return DicReader.getReader("company/company.data");
	}

	/**
	 * 机构名词典
	 * 
	 * @return
	 */
	public static BufferedReader getNewWordReader() {
		return DicReader.getReader("newWord/new_word_freq.dic");
	}

	/**
	 * 核心词典
	 * 
	 * @return
	 */
	public static BufferedReader getArraysReader() {
		return DicReader.getReader("arrays.dic");
	}

	/**
	 * 数字词典
	 * 
	 * @return
	 */
	public static BufferedReader getNumberReader() {
		return DicReader.getReader("numberLibrary.dic");
	}

	/**
	 * 英文词典
	 * 
	 * @return
	 */
	public static BufferedReader getEnglishReader() {
		return DicReader.getReader("englishLibrary.dic");
	}

	/**
	 * 词性表
	 * 
	 * @return
	 */
	public static BufferedReader getNatureMapReader() {
		return DicReader.getReader("nature/nature.map");
	}

	/**
	 * 词性关联表
	 * 
	 * @return
	 */
	public static BufferedReader getNatureTableReader() {
		return DicReader.getReader("nature/nature.table");
	}

	/**
	 * 得道姓名单字的词频词典
	 * 
	 * @return
	 */
	public static BufferedReader getPersonFreqReader() {
		return DicReader.getReader("person/name_freq.dic");
	}

	/**
	 * 名字词性对象反序列化
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, int[][]> getPersonFreqMap() {
		Map<String, int[][]> map = new HashMap<String, int[][]>(0);
		try (InputStream inputStream = DicReader.getInputStream("person/asian_name_freq.data")) {
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			map = (Map<String, int[][]>) objectInputStream.readObject();
		} catch (IOException e) {
			LOG.warn("IO异常", e);
		} catch (ClassNotFoundException e) {
			LOG.warn("找不到类", e);
		}
		return map;
	}

	/**
	 * 词与词之间的关联表数据
	 * 
	 * @return
	 */
	public static void initBigramTables() {
		try (BufferedReader reader = IOUtil.getReader(DicReader.getInputStream("bigramdict.dic"), "UTF-8")) {
			String temp = null;
			String[] strs = null;
			int freq = 0;
			while ((temp = reader.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				}
				strs = temp.split("\t");
				freq = Integer.parseInt(strs[1]);
				strs = strs[0].split("@");
				AnsjItem fromItem = DATDictionary.getItem(strs[0]);

				AnsjItem toItem = DATDictionary.getItem(strs[1]);

				if (fromItem == AnsjItem.NULL && strs[0].contains("#")) {
					fromItem = AnsjItem.BEGIN;
				}

				if (toItem == AnsjItem.NULL && strs[1].contains("#")) {
					toItem = AnsjItem.END;
				}

				if (fromItem == AnsjItem.NULL || toItem == AnsjItem.NULL) {
					continue;
				}

				if (fromItem.bigramEntryMap == null) {
					fromItem.bigramEntryMap = new HashMap<Integer, Integer>();
				}

				fromItem.bigramEntryMap.put(toItem.getIndex(), freq);

			}
		} catch (NumberFormatException e) {
			LOG.warn("数字格式异常", e);
		} catch (UnsupportedEncodingException e) {
			LOG.warn("不支持的编码", e);
		} catch (IOException e) {
			LOG.warn("IO异常", e);
		}
	}

	

	/**
	 * 加载歧义词典
	 * 
	 * @param modelName
	 * @return
	 */
	public static Forest ambiguity(String key) {
		String path = AMBIGUITY.get(fix("ambiguity_", key));

		if (path == null) {
			LOG.warn("ambiguity " + key + " not found in config ");
			return null;
		}
		Forest forest = (Forest) ALL.get(path);
		if (forest == null) {
			forest = initAmbiguity(key, path);
		}
		return forest;

	}

	/**
	 * 加载歧义词典
	 * 
	 * @param key
	 * @param path
	 * @return
	 */
	private synchronized static Forest initAmbiguity(String key, String path) {
		Forest forest = (Forest) ALL.get(path);
		if (forest != null) {
			return forest;
		}
		forest = new Forest();
		try (BufferedReader br = IOUtil.getReader(PathToStream.stream(path), "utf-8")) {
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
			LOG.info("load dic use time:" + (System.currentTimeMillis() - start) + " path is : " + path);
			ALL.put(path, forest);
			return forest;
		} catch (Exception e) {
			LOG.error("Init ambiguity library error :" + e.getMessage() + ", path: " + path);
			return null;
		}
	}

	/**
	 * 加载同义词典
	 * 
	 * @param modelName
	 * @return
	 */
	public static SmartForest<List<String>> synonyms(String key) {
		String path = SYNONYMS.get(fix("synonyms_", key));
		if (path == null) {
			LOG.warn("synonyms " + key + " not found in config ");
			return null;
		}
		@SuppressWarnings("unchecked")
		SmartForest<List<String>> forest = (SmartForest<List<String>>) ALL.get(path);
		if (forest == null) {
			forest = initSynonyms(key, path);
		}
		return forest;

	}

	/**
	 * 加载同义词典
	 * 
	 * @param key
	 * @param path
	 * @return
	 */
	private synchronized static SmartForest<List<String>> initSynonyms(String key, String path) {
		@SuppressWarnings("unchecked")
		SmartForest<List<String>> forest = (SmartForest<List<String>>) ALL.get(path);
		if (forest != null) {
			return forest;
		}
		forest = new SmartForest<>();

		LOG.info("begin init synonyms " + key);
		long start = System.currentTimeMillis();

		try (BufferedReader reader = IOUtil.getReader(PathToStream.stream(path), IOUtil.UTF8)) {
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
			LOG.info("load synonyms use time:" + (System.currentTimeMillis() - start) + " path is : " + path);
			return forest;
		} catch (Exception e) {
			LOG.error("Init synonyms library error :" + e.getMessage() + ", path: " + path);
			return null;
		}

	}

}
