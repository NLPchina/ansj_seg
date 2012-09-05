package org.ansj.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 加载词典用的类
 * @author ansj
 */
public class DicReader {
	public static BufferedReader getReader(String name){
		InputStream in = DicReader.class.getResourceAsStream(name) ;
		try {
			return new BufferedReader(new InputStreamReader(in,"UTF-8")) ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
}
