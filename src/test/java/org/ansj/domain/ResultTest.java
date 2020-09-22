package org.ansj.domain;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

/**
 * 返回结果的样式
 * @author ansj
 *
 */
public class ResultTest {

	
	@Test
	public void toStringTest() {
		System.out.println(ToAnalysis.parse("分词结果！").toString());
	}
	
	@Test
	public void toStringSplitTest() {
		System.out.println(ToAnalysis.parse("分词结果！").toString("\t"));
	}
	
	@Test
	public void toStringWithOutNatureTest() {
		System.out.println(ToAnalysis.parse("分词结果！").toStringWithOutNature());
	}
	
	
	@Test
	public void toStringWithOutNatureSplitTest() {
		System.out.println(ToAnalysis.parse("分词结果！").toStringWithOutNature("\t"));
	}

	@Test
	public void test1() {
		Result result = new Result(null);
		System.out.println(result.toStringWithOutNature("a"));
		Assert.assertEquals(result.toStringWithOutNature("a"), "");
	}
}
