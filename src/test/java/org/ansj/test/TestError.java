package org.ansj.test;

import org.ansj.dic.LearnTool;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = "怎好叫你空回去呢．" ;
		LearnTool tool = new LearnTool() ;
		System.out.println(NlpAnalysis.parse(str,tool));
		System.out.println(NlpAnalysis.parse("儿子道，`肋条离心甚远，怎么就好？'",tool));
		
	}
}
