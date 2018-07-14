package org.ansj.test;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

public class RealNameTest {

	@Test
	public void test(){
		MyStaticValue.isRealName = true ;

		Result parse = ToAnalysis.parse("据说ｗｉｎｄｏｗｓ９５推出前，１２３４５６ 半角打出的数字是这样的。123456 很一目了然吧！");

		System.out.println(parse.get(1).getRealName());
		System.out.println(parse);
	}

}
