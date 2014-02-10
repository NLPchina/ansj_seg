package org.ansj.test;

import java.util.List;

import junit.framework.Assert;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = " 恒大世俱杯首战对手出炉 非洲八冠王曾夺世界季军?" ;
		int len = 0 ;
		List<Term> parse = NlpAnalysis.parse(str);
		System.out.println(parse);
		for (Term term : parse) {
			len += term.getName().length() ;
		}
		Assert.assertEquals(len, str.length()); 
	}
}
