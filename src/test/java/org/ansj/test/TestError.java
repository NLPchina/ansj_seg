package org.ansj.test;

import java.util.List;

import junit.framework.Assert;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = "我很好奇你为什么不吃饭?" ;
		int len = 0 ;
		List<Term> parse = BaseAnalysis.parse(str);
		System.out.println(parse);
		for (Term term : parse) {
			len += term.getName().length() ;
		}
		Assert.assertEquals(len, str.length()); 
	}
}
