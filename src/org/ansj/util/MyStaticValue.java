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

import love.cq.domain.WoodInterface;
import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

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

	/**
	 * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
	 */
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
	 * 系统集成的补充词典
	 * 
	 * @return
	 */
	public static BufferedReader getSystemLibraryReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("systemLibrary.dic");
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
			result = new BigramEntry[423152][0];
			int fromId = 0;
			int toId = 0;
			int freq = 0;
			int length = 0;
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
}
