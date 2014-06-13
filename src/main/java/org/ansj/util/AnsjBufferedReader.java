package org.ansj.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.nlpcn.commons.lang.util.IOUtil;

/**
 * 我又剽窃了下jdk...职业嫖客 为了效率这个流的操作是不支持多线程的,要么就是长时间不写这种东西了。发现好费劲啊
 * 
 * @author ansj
 * 
 */
public class AnsjBufferedReader extends Reader {

	private Reader in;

	private char cb[];

	private static int defaultCharBufferSize = 8192;

	/**
	 * Creates a buffering character-input stream that uses an input buffer of
	 * the specified size.
	 * 
	 * @param in
	 *            A Reader
	 * @param sz
	 *            Input-buffer size
	 * 
	 * @exception IllegalArgumentException
	 *                If {@code sz <= 0}
	 */
	public AnsjBufferedReader(Reader in, int sz) {
		super(in);
		if (sz <= 0)
			throw new IllegalArgumentException("Buffer size <= 0");
		this.in = in;
		cb = new char[sz];
	}

	/**
	 * Creates a buffering character-input stream that uses a default-sized
	 * input buffer.
	 * 
	 * @param in
	 *            A Reader
	 */
	public AnsjBufferedReader(Reader in) {
		this(in, defaultCharBufferSize);
	}

	/** Checks to make sure that the stream has not been closed */
	private void ensureOpen() throws IOException {
		if (in == null)
			throw new IOException("Stream closed");
	}

	/**
	 * 为了功能的单一性我还是不实现了
	 */
	public int read(char cbuf[], int off, int len) throws IOException {
		throw new IOException("AnsjBufferedReader not support this interface! ");
	}

	private int start = 0;
	private int tempStart = 0;

	/**
	 * 读取一行数据。ps 读取结果会忽略 \n \r
	 */
	public String readLine() throws IOException {

		ensureOpen();
		
		start = tempStart ;

		StringBuilder sb = null;
		while (true) {

			tempLen = 0;
			ok = false;

			readString();

			if (!isRead && tempLen == 0) {
				if (sb != null) {
					return sb.toString();
				}
				return null;
			}

			if (!isRead) { // 如果不是需要读状态，那么返回
				tempStart += tempLen ;
				if (sb == null) {
					return new String(cb, tempOffe, tempLen);
				} else {
					sb.append(cb, tempOffe, tempLen);
					return sb.toString();
				}
			}

			// 如果是需要读状态那么读取
			if (sb == null) {
				sb = new StringBuilder();
			}
			sb.append(cb, tempOffe, tempLen);
			tempStart+=tempLen ;
		}

	}

	int offe = 0;
	int len = 0;

	boolean isRead = false;
	boolean ok = false;

	int tempOffe;
	int tempLen;

	private void readString() throws IOException {

		if (offe <= 0) {
			if (offe == -1) {
				isRead = false;
				return;
			}

			len = in.read(cb);
			if (len == 0) { // 说明到结尾了
				isRead = false;
				return;
			}
		}

		isRead = true;

		char c = 0;
		int i = offe;
		for (; i < len; i++) {
			c = cb[i];
			if (c != '\r' && c != '\n') {
				break;
			}
			start++ ;
		}

		offe = i;

		for (; i < len; i++) {
			c = cb[i];
			if (c == '\r' || c == '\n') {
				isRead = false;
				break;
			}
		}

		tempOffe = offe;
		tempLen = i - offe;

		if (offe == len) {
			if (len < cb.length) { // 说明到结尾了
				offe = -1;
			} else {
				offe = 0;
			}
		} else {
			offe = i;
		}

	}

	public void close() throws IOException {
		synchronized (lock) {
			if (in == null)
				return;
			try {
				in.close();
			} finally {
				in = null;
				cb = null;
			}
		}
	}

	public static void main(String[] args) throws IOException {

		String str = "中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf中国\r\n测试\n23、人、\r\r\nsdf";

		long start = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {

			 AnsjBufferedReader bufferedReader = new AnsjBufferedReader(new
			 StringReader(str));
//			BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
			String temp = null;
			while ((temp = bufferedReader.readLine()) != null) {
				System.out.println(temp);
			}
		}
		System.out.println(System.currentTimeMillis() - start);

		// long start = System.currentTimeMillis() ;
		// for (int i = 0; i < 10; i++) {
		// AnsjBufferedReader bufferedReader = new AnsjBufferedReader(new
		// InputStreamReader(new
		// FileInputStream("/home/ansj/data/allSportsArticle"),"utf-8"));
		// // BufferedReader bufferedReader =
		// IOUtil.getReader("/home/ansj/data/allSportsArticle", IOUtil.UTF8);
		// String temp = null;
		// while ((temp = bufferedReader.readLine()) != null) {
		// }
		// }
		// System.out.println(System.currentTimeMillis()-start);

		// long start = System.currentTimeMillis();
		// for (int i = 0; i < 1000000; i++) {
		// AnsjBufferedReader bufferedReader = new AnsjBufferedReader(new
		// StringReader(str), 3000);
		//
		// String temp = null;
		//
		// while ((temp = bufferedReader.readLine()) != null) {
		// }
		// }
		// System.out.println(System.currentTimeMillis()-start);
		//
		//
		// start = System.currentTimeMillis();
		// for (int i = 0; i < 1000000; i++) {
		// BufferedReader bufferedReader = new BufferedReader(new
		// StringReader(str), 3000);
		//
		// String temp = null;
		//
		// while ((temp = bufferedReader.readLine()) != null) {
		// }
		// }
		// System.out.println(System.currentTimeMillis()-start);
	}

}
