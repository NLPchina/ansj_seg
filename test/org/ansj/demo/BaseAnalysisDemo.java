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
		List<Term> paser = BaseAnalysis.paser("若雅虎关闭了,我就不访问网站了") ;
		System.out.println(paser);
	}
}
