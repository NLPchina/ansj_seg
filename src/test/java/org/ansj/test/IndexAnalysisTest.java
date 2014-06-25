package org.ansj.test;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.IndexAnalysis;

public class IndexAnalysisTest {
	public static void main(String[] args) {
//		System.out.println(IndexAnalysis.parse("这个问题经常的会被人提及。我一般会这样说，学习一种能让你开发大型系统的语言"));
//		System.out.println(ToAnalysis.parse("对了我现在有个demo想在vps上面展示...但是暂用内存太大..大家有什么好的建议么"));
//		System.out.println(IndexAnalysis.parse("工信处女干事"));
		System.out.println(IndexAnalysis.parse("Asdf as 12312 ")) ;
		System.out.println(IndexAnalysis.parse("Asdf as 12312 ")) ;
		
		
		System.out.println(IndexAnalysis.parse("季德胜蛇药片10片*6板 清热"));
		
		System.out.println(IndexAnalysis.parse("季德胜蛇药片"));

		UserDefineLibrary.insertWord("蛇药片", "n", 1000);
		
		System.out.println(IndexAnalysis.parse("季德胜蛇药片10片*6板 清热"));
		
		System.out.println(IndexAnalysis.parse("季德胜蛇药片"));
	}
}
