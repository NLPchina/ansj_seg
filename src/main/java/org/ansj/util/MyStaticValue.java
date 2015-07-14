package org.ansj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.dic.DicReader;
import org.ansj.domain.AnsjItem;
import org.ansj.library.DATDictionary;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 这个类储存一些公用变量.
 * 
 * @author ansj
 * 
 */
public class MyStaticValue {

	public static final Logger LIBRARYLOG = Logger.getLogger("DICLOG");

	// 是否开启人名识别
	public static boolean isNameRecognition = true;

	private static final Lock LOCK = new ReentrantLock();

	// 是否开启数字识别
	public static boolean isNumRecognition = true;

	// 是否数字和量词合并
	public static boolean isQuantifierRecognition = true;

	// crf 模型

	private static SplitWord crfSplitWord = null;

	public static boolean isRealName = false;

	/**
	 * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
	 */
	public static String userLibrary = "library/default.dic";

	public static String ambiguityLibrary = "library/ambiguity.dic";

	/**
	 * 是否用户辞典不加载相同的词
	 */
	public static boolean isSkipUserDefine = false;

	static {
		/**
		 * 配置文件变量
		 */
		try {
			ResourceBundle rb = ResourceBundle.getBundle("library");
			if (rb.containsKey("userLibrary"))
				userLibrary = rb.getString("userLibrary");
			if (rb.containsKey("ambiguityLibrary"))
				ambiguityLibrary = rb.getString("ambiguityLibrary");
			if (rb.containsKey("isSkipUserDefine"))
				isSkipUserDefine = Boolean.valueOf(rb.getString("isSkipUserDefine"));
			if (rb.containsKey("isRealName"))
				isRealName = Boolean.valueOf(rb.getString("isRealName"));
		} catch (Exception e) {
			LIBRARYLOG.warning("not find library.properties in classpath use it by default !");
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
		// TODO Auto-generated method stub
		return DicReader.getReader("arrays.dic");
	}

	/**
	 * 数字词典
	 * 
	 * @return
	 */
	public static BufferedReader getNumberReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("numberLibrary.dic");
	}

	/**
	 * 英文词典
	 * 
	 * @return
	 */
	public static BufferedReader getEnglishReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("englishLibrary.dic");
	}

	/**
	 * 词性表
	 * 
	 * @return
	 */
	public static BufferedReader getNatureMapReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("nature/nature.map");
	}

	/**
	 * 词性关联表
	 * 
	 * @return
	 */
	public static BufferedReader getNatureTableReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("nature/nature.table");
	}

	/**
	 * 得道姓名单字的词频词典
	 * 
	 * @return
	 */
	public static BufferedReader getPersonFreqReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("person/name_freq.dic");
	}

	/**
	 * 名字词性对象反序列化
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, int[][]> getPersonFreqMap() {
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		Map<String, int[][]> map = new HashMap<String, int[][]>(0);
		try {
			inputStream = DicReader.getInputStream("person/asian_name_freq.data");
			objectInputStream = new ObjectInputStream(inputStream);
			map = (Map<String, int[][]>) objectInputStream.readObject();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (objectInputStream != null)
					objectInputStream.close();
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 词与词之间的关联表数据
	 * 
	 * @return
	 */
	public static void initBigramTables() {
		BufferedReader reader = null;
		try {
			reader = IOUtil.getReader(DicReader.getInputStream("bigramdict.dic"), "UTF-8");
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
				
				if(fromItem.bigramEntryMap==null){
					fromItem.bigramEntryMap = new HashMap<Integer, Integer>() ;
				}

				fromItem.bigramEntryMap.put(toItem.index, freq) ;

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.close(reader);
		}
		
	}

	/**
	 * 得到默认的模型
	 * 
	 * @return
	 */
	public static SplitWord getCRFSplitWord() {
		// TODO Auto-generated method stub
		if (crfSplitWord != null) {
			return crfSplitWord;
		}
		LOCK.lock();
		if (crfSplitWord != null) {
			return crfSplitWord;
		}

		try {
			long start = System.currentTimeMillis();
			LIBRARYLOG.info("begin init crf model!");
			crfSplitWord = new SplitWord(Model.loadModel(DicReader.getInputStream("crf/crf.model")));
			LIBRARYLOG.info("load crf crf use time:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LOCK.unlock();
		}

		return crfSplitWord;
	}

}
