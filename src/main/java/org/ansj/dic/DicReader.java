package org.ansj.dic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 加载词典用的类
 * 
 * @author ansj
 */
public class DicReader {
	public static BufferedReader getReader(String name) {
		// maven工程修改词典加载方式
		InputStream in = DicReader.class.getResourceAsStream("/" + name);
		try {
			return new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getInputStream(String name) {
		// maven工程修改词典加载方式
		InputStream in = DicReader.class.getResourceAsStream("/" + name);
		return in;
	}
}
