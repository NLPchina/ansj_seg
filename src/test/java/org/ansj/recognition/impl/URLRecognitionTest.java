package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class URLRecognitionTest {
	@Test
	public void recognition() throws Exception {

		Result recognition = null ;

//		recognition = ToAnalysis.parse("http://www.baidu.com是一个好网址").recognition(new URLRecognition());
//
//		Assert.assertEquals(recognition.get(0).getName(), ("http://www.baidu.com"));


		recognition = ToAnalysis.parse("http://www.ansj-sun123.23423.com是一个好网址").recognition(new URLRecognition());

		System.out.println(recognition);

		Assert.assertEquals(recognition.get(0).getName(), ("http://www.ansj-sun123.23423.com"));


		recognition = ToAnalysis.parse("http://www.ansj-sun123.23423.com...是一个好网址").recognition(new URLRecognition());

		System.out.println(recognition);

		Assert.assertEquals(recognition.get(0).getName(), ("http://www.ansj-sun123.23423.com"));

		recognition = ToAnalysis.parse("http://localhost...是一个好网址").recognition(new URLRecognition());

		System.out.println(recognition);

		Assert.assertEquals(recognition.get(0).getName(), ("http://localhost"));


		recognition = ToAnalysis.parse("http://127.0.0.1...是一个好网址").recognition(new URLRecognition());

		System.out.println(recognition);

		Assert.assertEquals(recognition.get(0).getName(), ("http://127.0.0.1"));

		recognition = ToAnalysis.parse("http://...是一个好网址").recognition(new URLRecognition());

		System.out.println(recognition);

		Assert.assertEquals(recognition.get(0).getName(), ("http"));


	}

}