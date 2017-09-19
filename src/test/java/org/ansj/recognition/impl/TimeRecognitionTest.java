package org.ansj.recognition.impl;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class TimeRecognitionTest {
	
	@Test
	public void test() {
		
		TimeRecognition timeRecognition = new TimeRecognition() ;
		
		System.out.println(ToAnalysis.parse("5.1.3版本日期标注问题，2017年9月1日").recognition(timeRecognition));
		
		
	}


}
