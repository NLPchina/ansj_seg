package org.ansj.recognition.impl;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class UserDicNatureRecognitionTest {

	@Test
	public void test() {
		
		UserDicNatureRecognition userDicNatureRecognition = new UserDicNatureRecognition() ;
		
		System.out.println(ToAnalysis.parse("结婚的和尚未结婚的孙建是一个好人").recognition(userDicNatureRecognition));
		
		
	}

}
