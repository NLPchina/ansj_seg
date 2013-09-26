package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;

public class IndexPaserDemo {
	public static void main(String[] args) {
		System.out.println(IndexAnalysis.parse("上海虹桥机场南路"));
	}
}
