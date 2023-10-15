package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

/**
 * 身份证号码识别测试
 * 
 * @author Ansj
 *
 */
public class IDCardRecognitionTest {

	@Test
	public void test() {
		Result result = ToAnalysis.parse("我吃了一个西瓜，我今年25岁。13282619771220503X这里有一万个东西，我的身份证号码是130722198506280057h");
		Result result2 = ToAnalysis.parse("132826197713205030这，身份证号码是13072219850628005xx");

		System.out.println(result.recognition(new IDCardRecognition()));
		System.out.println(result2.recognition(new IDCardRecognition()));
	}

}
