package org.ansj.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;

import java.io.IOException;
import java.util.List;

public class hanLPDemo1 {
	public static void main(String[] args) throws IOException {

		System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！无锡通宝房地产有限公司\n" +
				"宁夏广洹工贸有限公司\n" +
				"苏州茶恩春茶业有限公司\n" +
				"东海县迅捷贸易有限公司"));

		List<Term> termList1 = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程无锡通宝房地产有限公司\n" +
				"宁夏广洹工贸有限公司\n" +
				"苏州茶恩春茶业有限公司\n" +
				"东海县迅捷贸易有限公司");
		System.out.println("termList1:" +termList1);

		Segment nShortSegment = new NShortSegment().enableCustomDictionary(true).enablePlaceRecognize(true).enableOrganizationRecognize(true);
		Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(true).enablePlaceRecognize(true).enableOrganizationRecognize(true);
		String[] testCase = new String[]{
				"今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
				"刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
				"无锡通宝房地产有限公司\n" +
						"宁夏广洹工贸有限公司\n" +
						"苏州茶恩春茶业有限公司\n" +
						"东海县迅捷贸易有限公司",
		};
		for (String sentence : testCase)
		{
			System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
		}

		//机构名称分词
		String[] testCase1 = new String[]{
				"我在上海林原科技有限公司兼职工作，",
				"我经常在台川喜宴餐厅吃饭，",
				"偶尔去地中海影城看电影。",
		};
		Segment segment = HanLP.newSegment().enableOrganizationRecognize(true);
		for (String sentence : testCase1)
		{
			List<Term> termList = segment.seg(sentence);
			System.out.println("机构名称分词:"+termList);
		}


		//关键词提取
		String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
		List<String> keywordList = HanLP.extractKeyword(content, 5);
		System.out.println(keywordList);

		//短语提取
		String text = "算法工程师\n" +
				"算法（Algorithm）是一系列解决问题的清晰指令，也就是说，能够对一定规范的输入，在有限时间内获得所要求的输出。如果一个算法有缺陷，或不适合于某个问题，执行这个算法将不会解决这个问题。不同的算法可能用不同的时间、空间或效率来完成同样的任务。一个算法的优劣可以用空间复杂度与时间复杂度来衡量。算法工程师就是利用算法处理事物的人。\n" +
				"\n" +
				"1职位简介\n" +
				"算法工程师是一个非常高端的职位；\n" +
				"专业要求：计算机、电子、通信、数学等相关专业；\n" +
				"学历要求：本科及其以上的学历，大多数是硕士学历及其以上；\n" +
				"语言要求：英语要求是熟练，基本上能阅读国外专业书刊；\n" +
				"必须掌握计算机相关知识，熟练使用仿真工具MATLAB等，必须会一门编程语言。\n" +
				"\n" +
				"2研究方向\n" +
				"视频算法工程师、图像处理算法工程师、音频算法工程师 通信基带算法工程师\n" +
				"\n" +
				"3目前国内外状况\n" +
				"目前国内从事算法研究的工程师不少，但是高级算法工程师却很少，是一个非常紧缺的专业工程师。算法工程师根据研究领域来分主要有音频/视频算法处理、图像技术方面的二维信息算法处理和通信物理层、雷达信号处理、生物医学信号处理等领域的一维信息算法处理。\n" +
				"在计算机音视频和图形图像技术等二维信息算法处理方面目前比较先进的视频处理算法：机器视觉成为此类算法研究的核心；另外还有2D转3D算法(2D-to-3D conversion)，去隔行算法(de-interlacing)，运动估计运动补偿算法(Motion estimation/Motion Compensation)，去噪算法(Noise Reduction)，缩放算法(scaling)，锐化处理算法(Sharpness)，超分辨率算法(Super Resolution),手势识别(gesture recognition),人脸识别(face recognition)。\n" +
				"在通信物理层等一维信息领域目前常用的算法：无线领域的RRM、RTT，传送领域的调制解调、信道均衡、信号检测、网络优化、信号分解等。\n" +
				"另外数据挖掘、互联网搜索算法也成为当今的热门方向。\n" +
				"算法工程师逐渐往人工智能方向发展。";
		List<String> phraseList = HanLP.extractPhrase(text, 10);
		System.out.println(phraseList);
	}



}
