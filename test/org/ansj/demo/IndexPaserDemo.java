package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;

public class IndexPaserDemo {
	public static void main(String[] args) {
		List<Term> paser = IndexAnalysis.paser("习近平") ;
		System.out.println(paser);
		System.out.println(IndexAnalysis.paser("2012年3月孙健很郁闷呢"));
	}
}
