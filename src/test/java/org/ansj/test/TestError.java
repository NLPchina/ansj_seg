package org.ansj.test;

import java.util.List;

import junit.framework.Assert;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = "n维空间F中的超平面是由方程定义的子集" ;
		int len = 0 ;
		LearnTool tool = new LearnTool() ;
		List<Term> parse = NlpAnalysis.parse(str,tool);
		System.out.println(parse);
		for (Term term : parse) {
			len += term.getName().length() ;
		}
		System.out.println(tool.getTopTree(100));; 
		Assert.assertEquals(len, str.length()); 
	}
}
