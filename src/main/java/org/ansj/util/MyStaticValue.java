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

import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.app.crf.model.CRFModel;
import org.ansj.dic.DicReader;
import org.ansj.domain.AnsjItem;
import org.ansj.library.DATDictionary;
import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.FileFinder;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个类储存一些公用变量.
 * 
 * @author ansj
 * 
 */
public class MyStaticValue {

	public static final Forest EMPTY_FOREST = new Forest();

	public static final Logger LIBRARYLOG = LoggerFactory.getLogger("DICLOG");

	public static final String DIC_DEFAULT = "dic";

	public static final String CRF_DEFAULT = "crf";

	// 是否开启人名识别
	public static Boolean isNameRecognition = true;

	// 是否开启数字识别
	public static Boolean isNumRecognition = true;

	// 是否数字和量词合并
	public static Boolean isQuantifierRecognition = true;

	// 是否显示真实词语
	public static Boolean isRealName = false;

	// 用户自定义词典
	public static final Map<String, Object> DIC = new HashMap<String, Object>();

	// CRF模型
	public static final Map<String, Object> CRF = new HashMap<String, Object>();

	/**
	 * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
	 */
	public static String ambiguityLibrary = "library/ambiguity.dic";
	
	/**
	 * 增加同义词词典路径变量
	 */
	public static String synonymsLibrary = "library/synonyms.dic";

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
					rb = new PropertyResourceBundle(
							IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
					LIBRARYLOG.info(
							"load ansj_library not find in classPath ! i find it in {} make sure it is your config!",
							find.getAbsolutePath());
				}
			} catch (Exception e1) {
				LIBRARYLOG.warn("not find ansj_library.properties. and err {} i think it is a bug!", e1.getMessage());
			}
		}

		if (rb == null) {
			try {
				rb = ResourceBundle.getBundle("library");
			} catch (Exception e) {
				try {
					File find = FileFinder.find("library.properties", 2);
					if (find != null && find.isFile()) {
						rb = new PropertyResourceBundle(
								IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
						LIBRARYLOG.info(
								"load library not find in classPath ! i find it in {} make sure it is your config!",
								find.getAbsolutePath());
					}
				} catch (Exception e1) {
					LIBRARYLOG.warn("not find library.properties. and err {} i think it is a bug!", e1.getMessage());
				}
			}
		}

		DIC.put(DIC_DEFAULT, "library/default.dic");

		if (rb == null) {
			LIBRARYLOG.warn("not find library.properties in classpath use it by default !");
		} else {

			for (String key : rb.keySet()) {

				if (key.equals("dic")) {
					DIC.put(key, rb.getString(key));
				} else if (key.equals("crf")) {
					CRF.put(key, rb.getString(key));
				} else if (key.startsWith("dic_")) {
					if (DIC.containsKey(key)) {
						LIBRARYLOG.warn("{} dic config repeat definition now overwrite it !", key);
					}
					DIC.put(key, rb.getString(key));
				} else if (key.startsWith("crf_")) {
					if (CRF.containsKey(key)) {
						LIBRARYLOG.warn("{} crf config repeat definition now overwrite it !", key);
					}
					CRF.put(key, rb.getString(key));
				} else {
					try {
						Field field = MyStaticValue.class.getField(key);
						field.set(null, ObjConver.conversion(rb.getString(key), field.getType()));
					} catch (NoSuchFieldException e) {
						LIBRARYLOG.error("not find field by {}", key);
					} catch (SecurityException e) {
						LIBRARYLOG.error("安全异常", e);
					} catch (IllegalArgumentException e) {
						LIBRARYLOG.error("非法参数", e);
					} catch (IllegalAccessException e) {
						LIBRARYLOG.error("非法访问", e);
					}
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
			LIBRARYLOG.warn("IO异常", e);
		} catch (ClassNotFoundException e) {
			LIBRARYLOG.warn("找不到类", e);
		}
		return map;
	}

	/**
	 * 词与词之间的关联表数据
	 * 
	 * @return
	 */
	public static void initBigramTables() {
		try (BufferedReader reader = IOUtil.getReader(DicReader.getInputStream("bigramdict.dic"), "UTF-8")){
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
			LIBRARYLOG.warn("数字格式异常", e);
		} catch (UnsupportedEncodingException e) {
			LIBRARYLOG.warn("不支持的编码", e);
		} catch (IOException e) {
			LIBRARYLOG.warn("IO异常", e);
		}
	}

	/**
	 * 得到默认的模型
	 *
	 * @return
	 */
	public static SplitWord getCRFSplitWord() {
		return getCRFSplitWord(CRF_DEFAULT);
	}

	/**
	 * 根据模型名称获取crf模型
	 * 
	 * @param key
	 * @return
	 */
	public static SplitWord getCRFSplitWord(String key) {
		Object temp = CRF.get(key);

		if (temp == null) {
			if (CRF_DEFAULT.equals(key)) { // 加载内置模型
				return initDefaultModel();
			} else {
				LIBRARYLOG.warn("crf {} not found in config ", key);
				return null;
			}
		} else if (temp instanceof String) {
			return initCRFModel(key, (String) temp);
		} else {
			return (SplitWord) temp;
		}
	}

	/**
	 * 加载默认的crf模型
	 * 
	 * @return
	 */
	private static synchronized SplitWord initDefaultModel() {

		Object obj = CRF.get(CRF_DEFAULT);
		if (obj != null && obj instanceof SplitWord) {
			return (SplitWord) obj;
		}
		try {
			LIBRARYLOG.info("init deafult crf model begin !");
			CRFModel model = new CRFModel(CRF_DEFAULT);
			model.loadModel(DicReader.getInputStream("crf.model"));
			SplitWord splitWord = new SplitWord(model);
			CRF.put(CRF_DEFAULT, splitWord);
			return splitWord;
		} catch (Exception e) {
			LIBRARYLOG.error("init err!", e);
		}
		return null;
	}

	/**
	 * 加载CRF模型
	 * 
	 * @param modelPath
	 * @return
	 */
	private static synchronized SplitWord initCRFModel(String key, String modelPath) {
		try {
			Object obj = CRF.get(key);
			if (obj != null && obj instanceof SplitWord) {
				return (SplitWord) obj;
			}
			if (new File(modelPath).isFile() && new File(modelPath).exists()) {
				long start = System.currentTimeMillis();
				LIBRARYLOG.info("begin init crf model!");
				SplitWord crfSplitWord = new SplitWord(Model.load(key, modelPath));
				CRF.put(key, crfSplitWord);
				LIBRARYLOG.info("load crf use time:{} path is : {}", System.currentTimeMillis() - start, modelPath);
				return crfSplitWord;
			} else {
				LIBRARYLOG.info("{} file  not found ,please make sure it is exists : {}", key, modelPath);
			}
		} catch (Exception e) {
			LIBRARYLOG.info("{} file : {} load err {}", key, modelPath, e.getMessage());
		}
		return null;
	}

	/**
	 * 得到默认的模型
	 *
	 * @return
	 */
	public static Forest getDicForest() {
		return getDicForest(DIC_DEFAULT);
	}

	/**
	 * 根据模型名称获取crf模型
	 * 
	 * @param modelName
	 * @return
	 */
	public static Forest getDicForest(String key) {
		Object temp = DIC.get(key);

		if (temp == null) {
			LIBRARYLOG.warn("dic {} not found in config ",key);
			return null;
		} else if (temp instanceof String) {
			return initForest(key, (String) temp);
		} else {
			return (Forest) temp;
		}
	}

	/**
	 * 用户自定义词典加载
	 * 
	 * @param key
	 * @param dicPath
	 * @return
	 */
	private synchronized static Forest initForest(String key, String dicPath) {
		Object obj = CRF.get(key);

		if (obj != null && obj instanceof Forest) {
			return (Forest) obj;
		}
		Forest forest = new Forest();
		UserDefineLibrary.loadLibrary(forest, dicPath);
		DIC.put(key, forest);
		return forest;
	}

}
