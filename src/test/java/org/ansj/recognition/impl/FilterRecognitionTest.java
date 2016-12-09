package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

/**
 * 停用词测试
 * @author Ansj
 *
 */
public class FilterRecognitionTest {

	@Test
	public void test() {
		String str = "我的小鸡鸡丢了!";

		Result parse = ToAnalysis.parse(str);

		System.out.println(parse);

		FilterRecognition fitler = new FilterRecognition();

		fitler.insertStopNatures("uj");
		fitler.insertStopNatures("ul");
		fitler.insertStopNatures("null");

		fitler.insertStopWords("我");

		fitler.insertStopRegexes("小.*?");

		Result modifResult = parse.recognition(fitler);

		for (Term term : modifResult) {
			Assert.assertNotSame(term.getNatureStr(), "uj");
			Assert.assertNotSame(term.getNatureStr(), "ul");
			Assert.assertNotSame(term.getNatureStr(), "null");
			Assert.assertNotSame(term.getName(), "我");
			Assert.assertNotSame(term.getName(), "小鸡鸡");
		}

		System.out.println(modifResult);
	}

}
