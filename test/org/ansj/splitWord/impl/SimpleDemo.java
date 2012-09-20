package org.ansj.splitWord.impl;

import java.io.IOException;

import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 最最最简单的分词调用方式
 * @author ansj
 *
 */
public class SimpleDemo {
	public static void main(String[] args) throws IOException {
		ToAnalysis.paser("init 123 孙建");
		int all =0 ;
		String str = "Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!" ;
		long start = System.currentTimeMillis() ;
		
		for (int i = 0; i < 100000; i++) {
			all += str.length() ;
			ToAnalysis.paser(str);
		}
		System.out.println("每秒处理了:"+(all*1000.0/(System.currentTimeMillis()-start)));
	}
}
