package org.ansj.test;

import java.util.List;

import junit.framework.Assert;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = "有些人，看起来客客气气、待人文明，但总给人很强的距离感，感觉他一定不想要被打扰，不会愿意和别人深交。然而，真有困难开口求救或者他主动帮忙，会出乎意料的热心肠和设身处地为人着想。于是你渐渐发现其实他并不是高冷，而是不习惯打交道怕太刻意会显得很蠢。" ;
		int len = 0 ;
		List<Term> parse = BaseAnalysis.parse(str);
		for (Term term : parse) {
			len += term.getName().length() ;
		}
		Assert.assertEquals(len, str.length()); 
	}
}
