package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

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

		StopRecognition fitler = new StopRecognition();

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
	
	@Test
	public void stopTest(){
		StopLibrary.insertStopWords(StopLibrary.DEFAULT, "的", "呵呵", "哈哈", "噢", "啊");
		Result terms = ToAnalysis.parse("英文版是小田亲自翻译的");
		//使用停用词
		System.out.println(terms.recognition(StopLibrary.get()));
	}

	@Test
	public void insertStopWordsTest() {
		String str = "我的小鸡鸡丢了!";
		Result parse = ToAnalysis.parse(str);
		System.out.println(parse);
		StopRecognition fitler = new StopRecognition();
		Collection<String> filterWords = new ArrayList<>();
		filterWords.add("我");
		fitler.insertStopWords(filterWords);
		Result modifResult = parse.recognition(fitler);
		for (Term term : modifResult) {
			Assert.assertNotSame(term.getName(), "我");
		}
		System.out.println(modifResult);
	}

	@Test
	public void clearTest() {
		String str = "我的小鸡鸡丢了!";
		Result parse = ToAnalysis.parse(str);
		System.out.println(parse);
		StopRecognition fitler = new StopRecognition();
		fitler.insertStopRegexes("小.*?");
		fitler.clear();
		Result modifResult = parse.recognition(fitler);
		for (Term term : modifResult) {
			Assert.assertNotSame(term.getName(), "小鸡鸡");
		}
		System.out.println(modifResult);
	}
}
