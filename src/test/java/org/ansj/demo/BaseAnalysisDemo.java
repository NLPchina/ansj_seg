package org.ansj.demo;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.BaseAnalysis;

/**
 * 只包含原则性分词和数字识别.
 * 
 * @author ansj
 */
public class BaseAnalysisDemo {
	public static void main(String[] args) {
		Result parse = BaseAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。新疆天拓工贸有限公司\n" +
				"成都星瑞农业有限公司\n" +
				"嵩县华伊印刷有限公司");
		System.out.println(parse);
	}
}
