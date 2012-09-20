package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.IOException;
import love.cq.util.IOUtil;

import org.ansj.splitWord.analysis.ToAnalysis;

public class FileDemo {
	public static void main(String[] args) throws IOException {
		StringBuilder sb = new StringBuilder() ;
		String temp = null ;
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/社交焦虑的治疗方式.txt", "GBK") ;
		while((temp=reader.readLine())!=null){
			sb.append(temp) ;
		}
		ToAnalysis.paser("test 123 孙") ;
		String str = sb.toString() ;
		long start = System.currentTimeMillis()  ;
		int allCount =0 ;
		for (int i = 0; i < 10000; i++) {
			allCount += str.length() ;
			ToAnalysis.paser(str) ;
		}
		long end = System.currentTimeMillis() ;
		System.out.println("共 "+allCount+" 个字符，每秒处理了:"+(allCount*1000.0/(end-start)));
	}
}
