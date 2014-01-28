package org.ansj.demo;

import java.io.BufferedReader;
import java.io.IOException;
import love.cq.util.IOUtil;

import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 对文件惊醒分词的例子
 * @author ansj
 *
 */

public class FileDemo {
	public static void main(String[] args) throws IOException {
		String temp = null;
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/3000_GB2312.txt", "GBK");
		ToAnalysis.parse("test 123 孙");
		long start = System.currentTimeMillis();
		int allCount = 0;
		while ((temp = reader.readLine()) != null) {
			allCount += temp.length();
			System.out.println(ToAnalysis.parse(temp));;
		}
		long end = System.currentTimeMillis();
		System.out.println(start - end);
		System.out.println("共 " + allCount + " 个字符，每秒处理了:" + (allCount * 1000.0 / (end - start)));
	}
}
