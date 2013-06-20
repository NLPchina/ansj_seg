package org.ansj.test;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class NlpTest {
	public static void main(String[] args) {
		// List<Term> paser = NlpAnalysis.parse("所有词性符合标准规范.属于系统识别出的词性有") ;
		// System.out.println(paser);
//		List<Term> paser = ToAnalysis.parse("北京理工大学办事处");
//		System.out.println(paser);
//		System.out.println(NlpAnalysis.parse("吴睿和张三是好朋友."));
		System.out.println(ToAnalysis.parse("怎么办"));
		System.out.println('\000'=='\u0000');
	}
}
