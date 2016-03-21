package org.ansj.test;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FilterModifWordTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String str = "我的小鸡鸡丢了!";
		List<Term> parse = ToAnalysis.parse(str);

		System.out.println(parse);

		UserDefineLibrary.insertWord("丢", "d1", 1000);

		FilterModifWord.insertStopNatures("uj");
		FilterModifWord.insertStopNatures("ul");
		FilterModifWord.insertStopNatures("null");

		FilterModifWord.insertStopWord("我");

		FilterModifWord.insertStopRegex("小.*?");

		List<Term> modifResult = FilterModifWord.modifResult(parse);

		for (Term term : modifResult) {
			Assert.assertNotSame(term.getNatureStr(), "uj");
			Assert.assertNotSame(term.getNatureStr(), "ul");
			Assert.assertNotSame(term.getNatureStr(), "null");
			Assert.assertNotSame(term.getName(), "我");
			Assert.assertNotSame(term.getName(), "小鸡鸡");
			Assert.assertSame("d1", term.getNatureStr());
		}

		UserDefineLibrary.insertWord("丢", "d2", 1000);
		
		modifResult = FilterModifWord.modifResult(modifResult);
		
		for (Term term : modifResult) {
			Assert.assertNotSame(term.getNatureStr(), "uj");
			Assert.assertNotSame(term.getNatureStr(), "ul");
			Assert.assertNotSame(term.getNatureStr(), "null");
			Assert.assertNotSame(term.getName(), "我");
			Assert.assertNotSame(term.getName(), "小鸡鸡");
			Assert.assertSame("d2", term.getNatureStr());
		}


		System.out.println(modifResult);
	}

}
