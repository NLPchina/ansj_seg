package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

/**
 * 只包含原则性分词和数字识别.
 * @author ansj
 */
public class BaseAnalysisDemo {
	public static void main(String[] args) {
		List<Term> parse = BaseAnalysis.parse("习近平和朱镕基关系很好!") ;
		System.out.println(parse);
	}
}
