package org.ansj.demo;

import org.ansj.splitWord.analysis.IndexAnalysis;

public class IndexPaserDemo {
	public static void main(String[] args) {
		System.out.println(IndexAnalysis.parse("上海虹桥机场南路"));
		System.out.println(IndexAnalysis.parse("主副食品"));
		System.out.println(IndexAnalysis.parse("成都星瑞农业有限公司嵩县华伊印刷有限公司花生油"));
		
	}
}
