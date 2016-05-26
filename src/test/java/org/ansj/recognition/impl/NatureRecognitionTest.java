package org.ansj.recognition.impl;

import org.ansj.splitWord.analysis.ToAnalysis;
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
		System.out.println(ToAnalysis.parse("结婚的和尚未结婚的孙建是一个好人").recognition(new NatureRecognition()));
	}

}
