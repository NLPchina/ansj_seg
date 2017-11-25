package org.ansj.recognition.arrimpl;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

/**
 * Created by Ansj on 26/09/2017.
 */
public class NumRecognitionTest {
	@Test
	public void test() {

		MyStaticValue.isRealName = false;
		System.out.println(ToAnalysis.parse("习近平和朱镕基情切照相"));
		System.out.println(ToAnalysis.parse("我有2323万多人次"));
		System.out.println(ToAnalysis.parse("我有2323亿多人次"));
		System.out.println(ToAnalysis.parse("一下2016年"));
		System.out.println(ToAnalysis.parse("实际上相对于此次未来AN TPY-2的天线指向"));
		System.out.println(ToAnalysis.parse("2中性粒细胞百分数NEUT%70.2040.00--75.00%\n").toString("\t"));
		System.out.println(ToAnalysis.parse("计划建立一个5万公顷面积的航天站一2月11日")); //#164
		System.out.println(ToAnalysis.parse("１２３４５６半角打出的数字是这样的。123456 "));
		System.out.println(ToAnalysis.parse("１２３４５６半角打出的数字是这样的。123456万个平方 "));
		System.out.println(ToAnalysis.parse("不可能出现3.５个人"));

		System.out.println(ToAnalysis.parse("零一二三五四六七九八十百千万亿"));
		System.out.println(ToAnalysis.parse("零壹贰叁肆伍陆柒捌玖拾佰仟万亿"));

		System.out.println(ToAnalysis.parse("6 666"));

		System.out.println(ToAnalysis.parse("905200.00 大写怎么写 是玖拾万伍仟贰佰元整 还是玖拾万零伍仟贰佰元整?"));

		System.out.println(ToAnalysis.parse("2017年2月12日是一个特殊的日子，我出生于1985年8月28日"));

		System.out.println(ToAnalysis.parse("http://www.ansj-sun123.23.423.com是一个网址"));


		System.out.println(ToAnalysis.parse("一九八一年是一个"));

		System.out.println(ToAnalysis.parse("一九八二年是一个"));

		System.out.println(ToAnalysis.parse("中华人民共和国万岁万岁万万岁"));

		System.out.println(ToAnalysis.parse("中华人民共和国万岁万岁万万岁"));

		System.out.println(ToAnalysis.parse("三个和尚抬水喝!"));

		System.out.println(ToAnalysis.parse("和天猫本年度"));


		System.out.println(ToAnalysis.parse("12,345.60元"));


		System.out.println(ToAnalysis.parse("3.2亿元"));

		System.out.println(ToAnalysis.parse("pm2.5平方公里含量20mg"));





	}
}
