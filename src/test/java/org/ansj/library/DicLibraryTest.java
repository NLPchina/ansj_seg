package org.ansj.library;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class DicLibraryTest {

	/**
	 * 关键词增加
	 *
	 * @param keyword 所要增加的关键词
	 * @param nature 关键词的词性
	 * @param freq 关键词的词频
	 */
	@Test
	public void insertTest() {
		DicLibrary.insert(DicLibrary.DEFAULT, "增加新词", "我是词性", 1000);
		Result parse = DicAnalysis.parse("这是用户自定义词典增加新词的例子");
		System.out.println(parse);
		boolean flag = false;
		for (Term term : parse) {
			flag = flag || "增加新词".equals(term.getName());
		}
		Assert.assertTrue(flag);

	}

	/**
	 * 增加关键词
	 *
	 * @param keyword
	 */
	@Test
	public void insertTest2() {
		DicLibrary.insert(DicLibrary.DEFAULT, "增加新词");
		Result parse = DicAnalysis.parse("这是用户自定义词典增加新词的例子");
		System.out.println(parse);
		boolean flag = false;
		for (Term term : parse) {
			flag = flag || "增加新词".equals(term.getName());
		}
		Assert.assertTrue(flag);
	}

	/**
	 * 删除关键词
	 */
	@Test
	public void delete() {
		insertTest();
		DicLibrary.delete(DicLibrary.DEFAULT, "增加新词");
		Result parse = DicAnalysis.parse("这是用户自定义词典增加新词的例子");
		System.out.println(parse);
		boolean flag = false;
		for (Term term : parse) {
			flag = flag || "增加新词".equals(term.getName());
		}
		Assert.assertFalse(flag);
	}

}
