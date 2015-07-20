package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.BaseAnalysis;

/**
 * 只包含原则性分词和数字识别.
 * 
 * @author ansj
 */
public class BaseAnalysisDemo {
	public static void main(String[] args) {
		List<Term> parse = BaseAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。");
		System.out.println(parse);
	}
}
