package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class EmailRecognitionTest {
	@Test
	public void recognition() throws Exception {

		Result recognition = null ;

		recognition = ToAnalysis.parse("ansj-sun@163.com是一个好网址").recognition(new EmailRecognition());

		System.out.println(recognition);

		Assert.assertEquals(recognition.get(0).getName(), ("ansj-sun@163.com"));

	}

}