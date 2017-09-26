package org.ansj.recognition.arrimpl;

import com.sun.corba.se.impl.oa.toa.TOA;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

/**
 * Created by Ansj on 26/09/2017.
 */
public class NumRecognitionTest {
	@Test
	public void test(){

		MyStaticValue.isRealName = false ;
		System.out.println(ToAnalysis.parse("一下2016年")); ;// #360
		System.out.println(ToAnalysis.parse("实际上相对于此次未来AN TPY-2的天线指向")); ;
		System.out.println(ToAnalysis.parse("2中性粒细胞百分数NEUT%70.2040.00--75.00%\n").toString("\t"));
		System.out.println(ToAnalysis.parse("计划建立一个5万公顷面积的航天站一2月11日")); //#164
		System.out.println(ToAnalysis.parse("１２３４５６半角打出的数字是这样的。123456 "));
		System.out.println(ToAnalysis.parse("１２３４５６半角打出的数字是这样的。123456万个平方 "));
		System.out.println(ToAnalysis.parse("不可能出现3.５个人"));

		System.out.println(ToAnalysis.parse("零一二三五四六七九八十百千万亿"));
		System.out.println(ToAnalysis.parse("零壹贰叁肆伍陆柒捌玖拾佰仟万亿"));

		System.out.println(ToAnalysis.parse("905200.00 大写怎么写 是玖拾万伍仟贰佰元整 还是玖拾万零伍仟贰佰元整?"));


	}
}
