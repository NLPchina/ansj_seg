package org.ansj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.dic.DicReader;
import org.ansj.domain.BigramEntry;
import org.ansj.library.InitDictionary;

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
    private static SplitWord minSplitWord = null;

    private static SplitWord bigSplitWord = null;

    /**
     * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
     */
    public static String userLibrary = "library/default.dic";

    public static String ambiguityLibrary = "library/ambiguity.dic";

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
    public static BigramEntry[][] getBigramTables() {
	BigramEntry[][] result = new BigramEntry[0][0];
	BufferedReader reader = null;
	try {
	    reader = IOUtil.getReader(DicReader.getInputStream("bigramdict.dic"), "UTF-8");
	    String temp = null;
	    String[] strs = null;
	    result = new BigramEntry[InitDictionary.arrayLength][0];
	    int fromId = 0;
	    int toId = 0;
	    int freq = 0;
	    BigramEntry to = null;
	    while ((temp = reader.readLine()) != null) {
		if (StringUtil.isBlank(temp)) {
		    continue;
		}

		strs = temp.split("\t");
		freq = Integer.parseInt(strs[1]);
		strs = strs[0].split("@");
		if ((fromId = InitDictionary.getWordId(strs[0])) <= 0) {
		    fromId = 0;
		}
		if ((toId = InitDictionary.getWordId(strs[1])) <= 0) {
		    toId = -1;
		}

		to = new BigramEntry(toId, freq);
		int index = Arrays.binarySearch(result[fromId], to);
		if (index > -1) {
		    continue;
		} else {
		    BigramEntry[] newBranches = new BigramEntry[result[fromId].length + 1];
		    int insert = -(index + 1);
		    System.arraycopy(result[fromId], 0, newBranches, 0, insert);
		    System.arraycopy(result[fromId], insert, newBranches, insert + 1, result[fromId].length - insert);
		    newBranches[insert] = to;
		    result[fromId] = newBranches;
		}

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
	return result;
    }

    /**
     * 得到默认的模型
     * 
     * @return
     */
    public static SplitWord getMinSplitWord() {
	// TODO Auto-generated method stub
	if (minSplitWord != null) {
	    return minSplitWord;
	}
	LOCK.lock();
	if (minSplitWord != null) {
	    return minSplitWord;
	}

	try {
	    LIBRARYLOG.info("begin init crf min model!");
	    minSplitWord = new SplitWord(Model.loadModel(DicReader.getInputStream("crf/min.model")));
	    LIBRARYLOG.info("load crf model min ok!");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    LOCK.unlock();
	}

	return minSplitWord;
    }

    /**
     * 得到默认的模型
     * 
     * @return
     */
    public static SplitWord getBigSplitWord() {
	// TODO Auto-generated method stub
	if (bigSplitWord != null) {
	    return bigSplitWord;
	}
	LOCK.lock();
	if (bigSplitWord != null) {
	    return bigSplitWord;
	}

	try {
	    LIBRARYLOG.info("begin init crf big model!");
	    bigSplitWord = new SplitWord(Model.loadModel(DicReader.getInputStream("crf/big.model")));
	    LIBRARYLOG.info("load crf crf big ok!");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    LOCK.unlock();
	}

	return bigSplitWord;
    }
}
