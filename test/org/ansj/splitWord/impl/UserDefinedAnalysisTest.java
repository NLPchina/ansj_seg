package org.ansj.splitWord.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import love.cq.library.Library;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;

public class UserDefinedAnalysisTest {

	public static void main(String[] args) throws IOException {

		// UserDefineLibrary.insertWord("我是特种兵","userDefine",100) ;
		//
		// System.out.println(ToAnalysis.paser("我是特种兵是一部很好看的电影!")); ;

		String format = "%s\tuserDefine\t10";
		List<String> dic = new ArrayList<String>();
		
		dic.add("贵州茅台") ;
		for (int i = 0; i < dic.size(); i++) {
			Library.insertWord(UserDefineLibrary.FOREST, String.format(format, new Object[] { dic.get(i) }));
		}
		
		System.out.println(ToAnalysis.paser("2003年至今，包括南方基金(微博)、大成基金(微博)、华宝兴业基金(微博)、富国基金(微博)、汇添富基金(微博)等多家大型基金公司旗下产品进入贵州茅台前十大流通股股东名单。贵州茅台股价历史走势图显示，在基金科瑞大比例持有贵州茅台期间，贵州茅台股价完成了一半的涨幅。而广发聚丰2008年开始大量持有贵州茅台，这是该股新一轮上涨的起点。不过，2006年汇添富开始陆续大量持有贵州茅台，则经历了该股猛烈的“过山车”行情。其中汇添富均衡增长大量持有期间，贵州茅台股价一度从204元一路跌至72.33元。"));

	}
}
