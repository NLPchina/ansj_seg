package org.ansj.util;

import java.io.BufferedReader;
import java.util.ResourceBundle;

import org.ansj.dic.DicReader;

/**
 * 这个类储存一些公用变量.
 * @author ansj
 *
 */
public class MyStaticValue {
	
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
	 * 词与词的关系词典
	 * 
	 * @return
	 */
	public static BufferedReader getBigramReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("bigramdict.dic");
	}

	public static BufferedReader getNatureMapReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("nature/nature.map");
	}
	
	public static BufferedReader getNatureTableReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("nature/nature.table");
	}
	
	public static BufferedReader getUserDefineReader() {
		// TODO Auto-generated method stub
		return DicReader.getReader("userLibrary.dic");
	}
}
