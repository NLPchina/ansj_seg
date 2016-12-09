package org.ansj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.ansj.dic.DicReader;
import org.ansj.domain.AnsjItem;
import org.ansj.library.DATDictionary;
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

	public static final Log LOG = LogFactory.getLog(MyStaticValue.class);

	// 是否开启人名识别
	public static Boolean isNameRecognition = true;

	// 是否开启数字识别
	public static Boolean isNumRecognition = true;

	// 是否数字和量词合并
	public static Boolean isQuantifierRecognition = true;

	// 是否显示真实词语
	public static Boolean isRealName = false;

	/**
	 * 是否用户辞典不加载相同的词
	 */
	public static boolean isSkipUserDefine = false;

	public static final Map<String, String> ENV = new HashMap<>();

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
				ENV.put(key, rb.getString(key));
				try {
					LOG.info("init " + key + " to env ");
					Field field = MyStaticValue.class.getField(key);
					field.set(null, ObjConver.conversion(rb.getString(key), field.getType()));
				} catch (Exception e) {
				}
			}

		}
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
	public static BufferedReader getNatureClassSuffix() {
		return DicReader.getReader("nature_class_suffix.txt");
	}

	/**
	 * 根据词语后缀判断词性
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

	/*
	 * 外部引用为了实例化加载变量
	 */
	public static Log getLog(Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}
}
