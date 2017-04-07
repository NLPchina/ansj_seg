package org.ansj.recognition.impl;

import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Test;

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
	
}
