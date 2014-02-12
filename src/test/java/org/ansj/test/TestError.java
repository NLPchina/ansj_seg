package org.ansj.test;

import java.util.List;

import junit.framework.Assert;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = "怎好叫你空回去呢．" ;
		int len = 0 ;
		LearnTool tool = new LearnTool() ;
		System.out.println(NlpAnalysis.parse(str,tool));
		System.out.println(NlpAnalysis.parse("儿子道，`肋条离心甚远，怎么就好？'",tool));
		
	}
}
