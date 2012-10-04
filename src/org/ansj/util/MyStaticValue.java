package org.ansj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.ansj.dic.DicReader;
import org.ansj.domain.BigramEntry;

/**
 * 这个类储存一些公用变量.
 * 
 * @author ansj
 * 
 */
public class MyStaticValue {

	public static String userDefinePath = null;

	/**
	 * 配置文件变量
	 */
	public final static ResourceBundle rb = ResourceBundle.getBundle("library");

	/**
	 * 人名词典
	 * 
	 * @return
	 */
	public static BufferedReader getPersonReader() {
		return DicReader.getReader("person/person.dic");
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
	 * 系统集成的补充词典
	 * 
	 * @return
	 */
	public static BufferedReader getUserDefineReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("userLibrary.dic");
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
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		BigramEntry[][] bigramTables = new BigramEntry[0][0];
		try {
			inputStream = DicReader.getInputStream("bigramdict.data");
			objectInputStream = new ObjectInputStream(inputStream);
			bigramTables = (BigramEntry[][]) objectInputStream.readObject();
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
		return bigramTables;
	}
}
