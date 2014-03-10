package org.ansj.test;

import org.ansj.dic.LearnTool;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		
		LearnTool tool = new LearnTool() ;
//		System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州",tool));
//		System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道",tool));
//		System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
		
		UserDefineLibrary.insertWord("日历", "n", 1000) ;
		
			System.out.println(ToAnalysis.parse("365日历，日历-万年历"));
			System.out.println(NlpAnalysis.parse("这次回家，我经济南下广州",tool));
			System.out.println(NlpAnalysis.parse("从古至今为何经济南强北弱?军事则北强南弱?_百度知道",tool));
			System.out.println(NlpAnalysis.parse("确保今年８％的增长速度"));
			System.out.println(NlpAnalysis.parse("越体越中意"));
			System.out.println(NlpAnalysis.parse("【万维网诞生25周年啦】1989年，《辛普森一家》走上银幕，“哈利波特”出生，伯纳斯-李发明万维网，并在1990年向世界免费公布了代码，把互联网从专业人士和少数狂热分子的数据传输系统转变为普通人的技术。1995年，42%的美国人从未听说过互联网，只有14%的人上过，2014年已达87%http://t.cn/8F1g3Mv"));
		}
}
