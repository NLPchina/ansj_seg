package org.ansj.test;

import java.util.ArrayList;
import java.util.List;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.library.DATDictionary;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

public class TestError {

	@Test
	public void test() throws Exception {

//		LearnTool tool = new LearnTool();
//		System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州", tool));
//		System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道", tool));
//		 System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
//		System.out.println(ToAnalysis.parse("美白面膜"));
//
//		UserDefineLibrary.insertWord("面膜", "n", 1000);
//		
////		System.out.println(ToAnalysis.parse("美白面膜"));
//		System.out.println("aa");
//		System.out.println(ToAnalysis.parse("999牌 感冒灵颗粒 10g*9包  解热镇痛，用于感冒引起的头痛、发热"));
//		
//		List<Term> parse = ToAnalysis.parse("我得了感冒") ;
//		System.out.println(parse);
//		
//		new NatureRecognition(parse).recognition();;
//		
//		System.out.println(parse);
//
//		
//		System.out.println(IndexAnalysis.parse("主副食品 软件设计"));
//		
//		System.out.println(ToAnalysis.parse("工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作"));
//		System.out.println(ToAnalysis.parse("365日历，日历-万年历"));
//		System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州", tool));
//		System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道", tool));
//		System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
//		System.out.println(NlpAnalysis.parse("越体越中意"));
//		System.out.println(NlpAnalysis.parse("在类似：1.三（接汉字数字时），会把这个汉字数字和1.放在一块作为一个数字 "));
//		System.out
//				.println(NlpAnalysis
//						.parse("一、概述正如上一篇博客，程序并没有主动设置PersonService实例的name属性值，而是通过Spring配置文件配置的，这就是说，PersonService实例的属性值并不是程序主动设置的，而是由Spring容器来负责注入的。在依赖注入的模式下，创建被调用者的工作不再由调用者来完成，因此称为控制反转（IoC）。创建被调用者实例的工作通常由Spring容器来完成，然后注入调用者，因此也称为......"));
//		System.out
//				.println(NlpAnalysis
//						.parse("【万维网诞生25周年啦】1989年，《辛普森一家》走上银幕，“哈利波特”出生，伯纳斯-李发明万维网，并在1990年向世界免费公布了代码，把互联网从专业人士和少数狂热分子的数据传输系统转变为普通人的技术。1995年，42%的美国人从未听说过互联网，只有14%的人上过，2014年已达87%http://t.cn/8F1g3Mv"));
//		System.out.println(NlpAnalysis.parse("西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区"));
//		System.out.println(NlpAnalysis.parse("西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区"));
//
//		System.out.println(ToAnalysis.parse("孙红雷暴打记者中国娱乐界如此蛮横"));
//
//		System.out.println(MyStaticValue.getCRFSplitWord().cohesion("念地"));
//		System.out.println(MyStaticValue.getCRFSplitWord().cohesion("地藏"));
//
//		// 歧义纠正
//		Value value = new Value("川府办", "川府办", "n");
//		Library.insertWord(UserDefineLibrary.ambiguityForest, value);
//
//		value = new Value("京财企业务", "京财企", "nt", "业务", "j");
//		Library.insertWord(UserDefineLibrary.ambiguityForest, value);
//
//		System.out.println(NlpAnalysis.parse("驻京办发文出来了"));
//		;
//		System.out.println(NlpAnalysis.parse("据说川府办发的发文很厉害"));
//		;
//		System.out.println(NlpAnalysis.parse("京财企业务繁忙"));
//		MyStaticValue.isNumRecognition = false ;
//		System.out.println(ToAnalysis.parse("0.46毫克"));
//		;
//		
//		
//
//		System.out.println(ToAnalysis.parse("上海马勒别墅"));
//		System.out.println(ToAnalysis.parse("电话卡+周杰伦摩天轮"));
//		System.out.println(ToAnalysis.parse("陆成恩和孙健是好朋友"));
//		System.out.println(ToAnalysis.parse("热海景区+"));
//		
//		UserDefineLibrary.insertWord("地黄丸", "aa", 1000);
//		
//		System.out.println(ToAnalysis.parse("同仁堂 六味地黄丸 30"));
//		System.out.println(ToAnalysis.parse("这样搜索曼秀雷敦肌研的东西也会出现"));
//		
//		System.out.println(ToAnalysis.parse("联合国;"));
//		
//		System.out.println(ToAnalysis.parse(";"));
//		
		List<String> all = new ArrayList<String>() ;
		
		all.add("某地区大地震后救灾工作程序示意图") ;
		all.add("大地震后") ;
		all.add("10,上城区小营街道大学路,余林,330102196204011513 ,2,13456808992,大学路新村44-122-102,大学路新村44-122-102,Z2015120110302017,Z,2015    -12-25") ;
		all.add("某品牌企业在京津冀地区建有饮用瓶装水厂") ;
		all.add("黄山16点前日出东北方") ;
		all.add("与其它洋流交汇的海域不易形成渔场") ;
		all.add("同类景观多出现在") ;
		all.add("据说ｗｉｎｄｏｗｓ９５推出前，考虑到低性能电脑安装它的时间很长，微软公司曾向心理学家请教，怎样在安装等待过程中设计出活动的画面才能让用户不致焦躁。") ;
		all.add("从古至今为何经济南强北弱?军事则北强南弱?_百度知道");
		all.add("孙红雷暴打记者中国娱乐界如此蛮横"); 
		all.add("发展中国家庭养猪事业") ;
		all.add("六味地黄丸软胶囊");
		all.add("8李建华、洪瑛,水澄花园北苑1幢2单元801室,浙AA09362220,南星街道,2006/11/28") ;
		all.add("浙杭上城结1997字第971662号,1997-10-06,,,上城区婚姻登记处,,1997-10-06,,,,李建华,,330102600702121,19600702,,,职员,,直大方伯八叉弄4号104室,,,,王桂花,,330106601004002,19601004,,,职工,,文三路4号217室,,,") ;
		all.add("10,上城区小营街道大学路,余林,330102196204011513 ,2,13456808992,大学路新村44-122-102,大学路新村44-122-102,Z2015120110302017,Z,2015-12-25");
		
		all.add("六味地黄丸") ;
		all.add("2015年6月3日") ;
//	    System.out.println(ToAnalysis.parse("你吃过了吗？？没吃"));
//	    System.out.println(NlpAnalysis.parse("你吃过了吗？？没吃"));
		
		
		for (String string : all) {
			System.out.println(ToAnalysis.parse(string));
			System.out.println(NlpAnalysis.parse(string));
			System.out.println(IndexAnalysis.parse(string));
		}
		
		System.out.println(DATDictionary.getItem(" "));
		System.out.println(DATDictionary.getItem("	"));


	}
}

