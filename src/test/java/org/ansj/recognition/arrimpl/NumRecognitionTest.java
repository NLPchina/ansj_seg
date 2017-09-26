package org.ansj.recognition.arrimpl;

import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

/**
 * Created by Ansj on 26/09/2017.
 */
public class NumRecognitionTest {
	@Test
	public void test(){
		System.out.println(ToAnalysis.parse("一下2016年")); ;// #360
		System.out.println(ToAnalysis.parse("实际上相对于此次未来AN TPY-2的天线指向")); ;
		System.out.println(ToAnalysis.parse("2中性粒细胞百分数NEUT%70.2040.00--75.00%\n"));
		System.out.println(ToAnalysis.parse("计划建立一个5万公顷面积的航天站一2月11日")); //#164

	}
}
