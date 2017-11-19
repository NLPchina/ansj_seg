package org.ansj.recognition.arrimpl;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class PersonRecognitionTest {

	@Test
	public void test() {
		System.out.println(ToAnalysis.parse("王总和小丽结婚了"));
		System.out.println(ToAnalysis.parse("俞志龙和陈举亚是南京维数公司的同事"));
		System.out.println(ToAnalysis.parse("邓颖超生前使用的物品"));
		System.out.println(ToAnalysis.parse("费孝通向人大常委会提交书面报告"));
		System.out.println(ToAnalysis.parse("徐德有说这是一个好日子"));
		System.out.println(ToAnalysis.parse("政务司司长陈方安生出任委员会主席"));
		System.out.println(ToAnalysis.parse("签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。"));
		System.out.println(ToAnalysis.parse("王国强、高峰、汪洋、张朝阳、韩寒、小四"));
		System.out.println(ToAnalysis.parse("张浩和胡健康复员了"));
		System.out.println(ToAnalysis.parse("这里有关天培的壮烈"));
		System.out.println(ToAnalysis.parse("龚学平等领导,邓颖超生前"));

		System.out.println(ToAnalysis.parse("欧阳娜娜借了长孙无忌三块钱"));
		System.out.println(ToAnalysis.parse("证明原告孙辉与被告姚宏旭之间的借款关系。"));
		System.out.println(ToAnalysis.parse("谭彬林工号,杨军工号,陆江工号,姚曼青工号"));


	}
}
