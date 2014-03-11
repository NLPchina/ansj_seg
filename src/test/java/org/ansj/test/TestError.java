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
			System.out.println(NlpAnalysis.parse("一、概述正如上一篇博客，程序并没有主动设置PersonService实例的name属性值，而是通过Spring配置文件配置的，这就是说，PersonService实例的属性值并不是程序主动设置的，而是由Spring容器来负责注入的。在依赖注入的模式下，创建被调用者的工作不再由调用者来完成，因此称为控制反转（IoC）。创建被调用者实例的工作通常由Spring容器来完成，然后注入调用者，因此也称为......"));
		}
}
