package org.ansj.recognition.impl;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 词性识别测试
 * 
 * @author Ansj
 *
 */
public class NatureRecognitionTest {

	@Test
	public void test() {
		System.out.println(NlpAnalysis.parse("结婚的和尚未结婚的孙建是一个好人").recognition(new NatureRecognition()));
	}
	
	@Test
	public void natureGuess() {
		System.out.println(NatureRecognition.guessNature("北京英富森股份有限公司").nature);
		System.out.println(NatureRecognition.guessNature("尹科").nature);
		System.out.println(NatureRecognition.guessNature("保福寺桥").nature);
		System.out.println(NatureRecognition.guessNature("爱丽丝布衣诺夫").nature);
	}

	@Test
	public void recognitionList() {
		String string = "结婚的和尚未结婚的孙建是一个好人";
		List<String> words = new ArrayList<>();
		words.add(string);
		List<Term> terms = new NatureRecognition().recognition(words);
		System.out.println(terms.get(0).toString());
		Assert.assertEquals(terms.get(0).toString(), string);
	}

	@Test
	public void recognitionListAndInt() {
		String string = "结婚的和尚未结婚的孙建是一个好人";
		List<String> words = new ArrayList<>();
		words.add(string);
		List<Term> terms = new NatureRecognition().recognition(words, 0);
		System.out.println(terms.get(0).toString());
		Assert.assertEquals(terms.get(0).toString(), string);
	}
}
