package org.ansj.test;

import org.ansj.dic.LearnTool;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		
		LearnTool tool = new LearnTool() ;
//		System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州",tool));
//		System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道",tool));
//		System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
		
		UserDefineLibrary.insertWord("日历", "n", 1000) ;
		
		System.out.println(ToAnalysis.parse("365日历，日历-万年历"));
		System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州",tool));
		System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道",tool));
		System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
		System.out.println(NlpAnalysis.parse("越体越中意"));
	}
}
