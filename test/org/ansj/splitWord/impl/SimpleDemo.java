package org.ansj.splitWord.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 最最最简单的分词调用方式
 * @author ansj
 *
 */
public class SimpleDemo {
	public static void main(String[] args) throws IOException {
		List<Term> paser = ToAnalysis.paser("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
		System.out.println(paser);
	}
}
