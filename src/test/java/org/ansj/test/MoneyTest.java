package org.ansj.test;

import org.ansj.splitWord.analysis.NlpAnalysis;

public class MoneyTest {
	public static void main(String[] args) throws Exception {
		System.out.println("最大可用内存" + (Runtime.getRuntime().maxMemory() / 1000000));
		System.out.println("当前JVM空闲内存" + (Runtime.getRuntime().freeMemory() / 1000000));
		System.out.println("当前JVM占用的内存总数" + (Runtime.getRuntime().totalMemory() / 1000000));

		long begin = (Runtime.getRuntime().freeMemory() / 1000000);

		NlpAnalysis.parse("江苏宏宝五金股份有限公司（以下简称“本公司”）于2012年11月9日接到实际控制人") ;

		System.out.println("分词系统使用了的内存" + (begin - Runtime.getRuntime().freeMemory() / 1000000));
		System.out.println("最大可用内存" + (Runtime.getRuntime().maxMemory() / 1000000));
		System.out.println("当前JVM空闲内存" + (Runtime.getRuntime().freeMemory() / 1000000));
		System.out.println("当前JVM占用的内存总数" + (Runtime.getRuntime().totalMemory() / 1000000));
	}
}
