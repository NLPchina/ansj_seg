package org.ansj.splitWord.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

public class Test {

	public static void main(String[] args) throws IOException {
		String str = null;

		// BufferedReader br =
		// IOUtil.getReader("C://Users//ansj//Desktop//新建文本文档.txt", "GBK") ;
		String[] strs = new String[100];
		strs[0] = "他说的确实在理";
		strs[1] = "长春市长春节讲话";
		strs[2] = "结婚的和尚未结婚的";
		strs[3] = "结合成分子时";
		strs[4] = "旅游和服务是最好的";
		strs[5] = "邓颖超生前最喜欢的一个东西";
		strs[6] = "中国航天官员应邀到美国与太空总署官员开会";
		strs[7] = "上海大学城书店";
		strs[8] = "北京大学生前来应聘";
		strs[9] = "中外科学名著";
		strs[10] = "为人民服务";
		strs[11] = "独立自主和平等互利的原则";
		strs[12] = "为人民办公益";
		strs[13] = "这事的确定不下来";
		strs[14] = "费孝通向人大常委会提交书面报告";
		strs[15] = "aaa分事实上发货丨和无哦喝完酒";
		strs[16] = "不好意思清清爽爽";
		strs[17] = "长春市春节讲话";
		strs[18] = "中华人民共和国万岁万岁万万岁";
		strs[19] = "检察院鲍绍检察长就是在世诸葛.像诸葛亮一样聪明";
		strs[20] = "长春市长春药店";
		strs[21] = "乒乓球拍卖完了";
		strs[22] = "计算机网络管理员用虚拟机实现了手机游戏下载和开源项目的管理金山毒霸";
		strs[23] = "长春市长春药店";
		strs[29] = "胡锦涛与神九航天员首次实现天地双向视频通话";
		strs[30] = "mysql不支持 同台机器两个mysql数据库之间做触发器";
		strs[31] = "孙建是一个好人.他和蔡晴是夫妻两 ,对于每一本好书他都原意一一读取..他们都很喜欢元宵.康燕和他们住在一起.我和马春亮,韩鹏飞都是好朋友,不知道什么原因";
		strs[32] = "一年有三百六十五个日出 我送你三百六十五个祝福 时钟每天转了一千四百四十圈我的心每天都藏着 一千四百四十多个思念 每一天都要祝你快快乐乐  每一分钟都盼望你平平安安 吉祥的光永远环绕着你 像那旭日东升灿烂无比 ";
		strs[32] = " 一年有三百六十五个日出";
		strs[33] = "学校学费要一次性交一千元";
		strs[34] = "发展中国家庭养猪事业";
		strs[35] = "安徽省是一个发展中的省";
		strs[36] = "北京理工大学办事处";
		strs[37] = "上海大学城";
		strs[38] = "脚下的一大块方砖地面";
		strs[39] = "程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪";
		strs[40] = "中文分词 是一个实现,中文分词是一个实现";
		strs[41] = "吃葡萄牙酸";
		strs[42] = "葡萄牙的进攻";
		strs[43] = "凭医生处方才可购买";
		strs[44] = "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作";
		strs[45] = "门把手坏了,门把手夹了";
		strs[46] = "一个和尚挑水喝,两个和尚抬水喝,三个和尚无水喝 , 一次性交易就毁了施水才";
		strs[47] = "老乡们决定把他重新点燃起来";
		strs[48] = "我的同事张三和李四是夫妻";
		strs[49] = "下面就为大家整理了央视主持人的帅仔 靓女曝光照";

		strs[50] = "两毛五一斤.一斤八两";
		strs[51] = "在训练中将知识巩固";
		strs[52] = "中将孙建很厉害";

		strs[53] = "审讯室里一直陪着我们的两个警察";
		strs[54] = "一只胳膊两个警察";
		strs[55] = "c语言怎么读写ini文件";
		strs[56] = "关卡编辑器";
		strs[57] = "eclipse 多项目依赖";
		strs[58] = "张媛:猩猩的娃叫陈美希吗？不知为什么让我突然想起了林明美";

		List all = null;
		long start = System.currentTimeMillis();
		int count = 0;
		for (int mm = 0; mm < 1; mm++) {
			for (int i = 0; i < strs.length; i++) {
				all = new ArrayList();
				if (strs[i] == null)
					continue;
				Analysis udf = new ToAnalysis(new StringReader(strs[i]));
				Term term = null;
				while ((term = udf.next()) != null) {
					all.add(term);
				}
				new NatureRecognition(all).recogntion();
				System.out.println(all);
			}
		}
		System.out.println(System.currentTimeMillis() - start);

		// strs[99] = "程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪";
		// start = System.currentTimeMillis();
		// for (int i = 0; i < 100000; i++) {
		// ToAnalysis.paser(strs[99]) ;
		// // for (SegToken segToken : process) {
		// // System.out.print(new String(segToken.charArray));
		// // System.out.print(" ");
		// // }
		// // System.out.println();
		// }
		// System.out.println(System.currentTimeMillis() - start);
	}
}
