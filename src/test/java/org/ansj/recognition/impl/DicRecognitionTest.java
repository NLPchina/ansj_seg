package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class DicRecognitionTest {
	@Test
	public void recognition() throws Exception {

		String str = "眼泪无珠眼浅皮薄眼看四方眼看四面";

		Result parse = BaseAnalysis.parse(str);

		System.out.println(parse);

		new DicRecognition(DicLibrary.get()).recognition(parse);

		System.out.println(parse);

		Assert.assertEquals(parse.toString(), ToAnalysis.parse(str).toString());
	}

}