package org.ansj.test;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class TestError {

	@Test
	public void test() {

//		LearnTool tool = new LearnTool();
		// System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州",tool));
		// System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道",tool));
		// System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
//		System.out.println(ToAnalysis.parse("美白面膜"));
//
//		UserDefineLibrary.insertWord("面膜", "n", 1000);
//		
////		System.out.println(ToAnalysis.parse("美白面膜"));
//		System.out.println("aa");
//		System.out.println(ToAnalysis.parse("999牌 感冒灵颗粒 10g*9包  解热镇痛，用于感冒引起的头痛、发热"));
		
//		List<Term> parse = ToAnalysis.parse("我得了感冒") ;
//		System.out.println(parse);
//		
//		new NatureRecognition(parse).recognition();;
//		
//		System.out.println(parse);
//
//		
//		System.out.println(IndexAnalysis.parse("主副食品 软件设计"));
		
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
		
		

//		System.out.println(ToAnalysis.parse("上海马勒别墅"));
//		System.out.println(ToAnalysis.parse("电话卡+周杰伦摩天轮"));
//		System.out.println(ToAnalysis.parse("陆成恩和孙健是好朋友"));
//		System.out.println(ToAnalysis.parse("热海景区+"));
//		
//		UserDefineLibrary.insertWord("地黄丸", "aa", 1000);
//		
//		System.out.println(ToAnalysis.parse("同仁堂 六味地黄丸 30"));
//		System.out.println(ToAnalysis.parse("这样搜索曼秀雷敦肌研的东西也会出现"));
		
		System.out.println(ToAnalysis.parse("联合国;"));
		
		System.out.println(ToAnalysis.parse(";"));
		
		
	}
}
