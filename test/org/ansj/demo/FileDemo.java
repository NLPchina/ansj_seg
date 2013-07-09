package org.ansj.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.splitWord.analysis.ToAnalysis;

public class FileDemo {
	public static void main(String[] args) throws IOException {
		StringBuilder sb = new StringBuilder() ;
		String temp = null ;
		
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/3000_GB2312.txt", "GBK") ;
		ToAnalysis.parse("test 123 孙") ;
		long start = System.currentTimeMillis()  ;
		int allCount =0 ;
		while((temp=reader.readLine())!=null){
//			sb.append(temp) ;
			allCount += temp.length() ;
			ToAnalysis.parse(temp) ;
		}
//		ToAnalysis.parse("test 123 孙") ;
//		String str = sb.toString() ;
//		long start = System.currentTimeMillis()  ;
		
//		for (int i = 0; i < 10000; i++) {
//			allCount += str.length() ;
//			ToAnalysis.parse(str) ;
//		}
		long end = System.currentTimeMillis() ;
		System.out.println(start-end);
		System.out.println("共 "+allCount+" 个字符，每秒处理了:"+(allCount*1000.0/(end-start)));
	}
}
