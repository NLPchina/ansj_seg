package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class IPRecognitionTest {

	@Test
	public void recognition() throws Exception {

		Result recognition = null ;
		recognition = ToAnalysis.parse("192.168.1.1, 1.1.1.1, 255.254.251.256, 0.0.0.0").recognition(new IPRecognition());

		System.out.println(recognition);
		Assert.assertEquals(recognition.get(0).getName(), ("192.168.1.1"));
	}

}