package org.ansj.demo;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 最最最简单的分词调用方式
 * 
 * @author ansj
 * 
 */
public class SimpleDemo {
	public static void main(String[] args) throws IOException {
		String str = "Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!";
		List<Term> paser = BaseAnalysis.paser(str);
		System.out.println(paser);
	}
}
