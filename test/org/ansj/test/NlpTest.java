package org.ansj.test;

import org.ansj.splitWord.analysis.ToAnalysis;

public class NlpTest {
	public static void main(String[] args) {
		// List<Term> parse = NlpAnalysis.parse("所有词性符合标准规范.属于系统识别出的词性有") ;
		// System.out.println(parse);
//		List<Term> parse = ToAnalysis.parse("北京理工大学办事处");
//		System.out.println(parse);
//		System.out.println(NlpAnalysis.parse("吴睿和张三是好朋友."));
		System.out.println(ToAnalysis.parse("怎么办"));
		System.out.println('\000'=='\u0000');
	}
}
