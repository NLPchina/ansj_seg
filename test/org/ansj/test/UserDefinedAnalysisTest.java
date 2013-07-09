package org.ansj.test;

import java.util.List;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;

public class UserDefinedAnalysisTest {

	// public static void main(String[] args) throws IOException {
	//
	// // UserDefineLibrary.insertWord("我是特种兵","userDefine",100) ;
	// //
	// // System.out.println(ToAnalysis.parse("我是特种兵是一部很好看的电影!")); ;
	//
	// String format = "%s\tuserDefine\t10";
	// List<String> dic = new ArrayList<String>();
	//
	// dic.add("贵州茅台") ;
	//
	// dic.add("万科a") ;
	// dic.add("尼康") ;
	// dic.add("上海电力") ;
	// for (int i = 0; i < dic.size(); i++) {
	// Library.insertWord(UserDefineLibrary.FOREST, String.format(format, new
	// Object[] { dic.get(i) }));
	// }
	// List terms = ToAnalysis.parse("就是加了一个上海电力，它没有生效，还是会分成上海/电力");
	// (new NatureRecognition(terms)).recognition();
	// System.out.println(terms);
	//
	//
	// UserDefineLibrary.insertWord("ansj中文分词1", "userDefine", 1000);
	//
	// LearnTool learn = new LearnTool() ;
	//
	// System.out.println(NlpAnalysis.parse("2003年ansj中文分词1至今，包括南方基金(微博)、大成基金(微博)、 万科A 万科ａ 万科a 万科A华宝兴业基金(微博)、富国基金(微博)、汇添富基金(微博)等多家大型基金公司旗下产品进入贵州茅台前十大流通股股东名单。贵州茅台股价历史走势图显示，在基金科瑞大比例持有贵州茅台期间，贵州茅台股价完成了一半的涨幅。而广发聚丰2008年开始大量持有贵州茅台，这是该股新一轮上涨的起点。不过，2006年汇添富开始陆续大量持有贵州茅台，则经历了该股猛烈的“过山车”行情。其中汇添富均衡增长大量持有期间，贵州茅台股价一度从204元一路跌至72.33元。",learn));
	//
	// System.out.println(learn.count+"\t"+learn.getTopTree(0));
	//
	// }

	public static void main(String[] args) {
		String str = "Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!";
		String str1 = "上海电力2012年财务报表如下怎么办";
		
		
		
		UserDefineLibrary.insertWord("新词", "词性", 1000) ;
	
		List parse = ToAnalysis.parse(str1);
		System.out.println(parse);
	}
}
