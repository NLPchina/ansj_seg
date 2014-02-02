package org.ansj.demo;

import java.util.List;

import org.ansj.util.MyStaticValue;

public class CRFSegDemo {
	public static void main(String[] args) {
		List<String> cut = MyStaticValue.getCRFSplitWord().cut("数据专家跨界的典范，不得不提到火箭队总经理莫雷。他没参加过职业篮球比赛，并说他“对数字的兴趣远远大于任何一项运动。”从MIT毕业开过体育分析公司和担任球队助理了解行业后，他在火箭队将魔球理论发扬光大，用数据模型淘性价比最好的新秀。他的数据团队如今被纷纷挖到别的球队当总经理或数据老大");
		
		System.out.println(cut);
	}
}
