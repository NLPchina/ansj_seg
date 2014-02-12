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
		System.out.println(NlpAnalysis.parse(str,tool));
		System.out.println(NlpAnalysis.parse(str));
		
	}
}
