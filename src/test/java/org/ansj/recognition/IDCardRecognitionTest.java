package org.ansj.recognition;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.IDCardRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class IDCardRecognitionTest {

	@Test
	public void test() {
		List<Term> parse = ToAnalysis.parse("我吃了一个西瓜，我今年25岁。13282619771220503X这里有一万个东西，我的身份证号码是130722198506280057h");

		List<Term> recognition = IDCardRecognition.recognition(parse);

		System.out.println(recognition);
	}

}
