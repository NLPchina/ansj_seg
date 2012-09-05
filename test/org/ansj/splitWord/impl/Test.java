package org.ansj.splitWord.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class Test {
	
	public static void main(String[] args) throws IOException {
		String str = null ;
		
//		BufferedReader br = IOUtil.getReader("C://Users//ansj//Desktop//新建文本文档.txt", "GBK") ;
		String[] strs = new String[100] ;
		strs[0] = "他说的确实在理" ;
		strs[1] = "长春市长春节讲话" ;
		strs[2] = "结婚的和尚未结婚的" ;
		strs[3] = "结合成分子时" ;
		strs[4] = "旅游和服务是最好的" ;
		strs[5] = "邓颖超生前最喜欢的一个东西" ;
		strs[6] = "中国航天官员应邀到美国与太空总署官员开会" ;
		strs[7] = "上海大学城书店" ;
		strs[8] = "北京大学生前来应聘" ;
		strs[9] = "中外科学名著" ;
		strs[10] = "为人民服务" ;
		strs[11] = "独立自主和平等互利的原则" ;
		strs[12] = "为人民办公益" ;
		strs[13] = "这事的确定不下来" ;
		strs[14] = "费孝通向人大常委会提交书面报告" ;
		strs[15] = "aaa分事实上发货丨和无哦喝完酒" ;
		strs[16] = "不好意思清清爽爽" ;
		strs[17] = "长春市春节讲话" ;
		strs[18] = "中华人民共和国万岁万岁万万岁" ;
		strs[19] = "检察院鲍绍检察长就是在世诸葛.像诸葛亮一样聪明" ;
		strs[20] = "长春市长春药店" ;
		strs[21] = "乒乓球拍卖完了" ;
		strs[22] = "计算机网络管理员用虚拟机实现了手机游戏下载和开源项目的管理金山毒霸" ;	
		strs[23] = "长春市长春药店" ;
		strs[29] = "胡锦涛与神九航天员首次实现天地双向视频通话" ;
		strs[30] = "mysql不支持 同台机器两个mysql数据库之间做触发器" ;
		strs[31] = "孙建是一个好人.他和蔡晴是夫妻两 ,对于每一本好书他都原意一一读取..他们都很喜欢元宵.康燕和他们住在一起.我和马春亮,韩鹏飞都是好朋友,不知道什么原因" ;
		strs[32] = "一年有三百六十五个日出 我送你三百六十五个祝福 时钟每天转了一千四百四十圈我的心每天都藏着 一千四百四十多个思念 每一天都要祝你快快乐乐  每一分钟都盼望你平平安安 吉祥的光永远环绕着你 像那旭日东升灿烂无比 " ;
		strs[33] = "学校学费要一次性交一千元" ;
		strs[34] = "发展中国家庭养猪事业" ;
		strs[35] = "安徽省是一个发展中的省" ;
		strs[36] = "北京理工大学办事处" ;
		strs[0] = "上海大学城" ;
		strs[0] = "脚下的一大块方砖地面" ;
		strs[99] = "祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪" ;
		
		strs[98] = "陈刚" ;

		strs[37] ="审讯室里一直陪着我们的两个警察" ;
		strs[38] ="一只胳膊两个警察" ;
		strs[39] ="c语言怎么读写ini文件" ;
		strs[40] ="关卡编辑器" ;
		strs[41] = "eclipse 多项目依赖" ;
		strs[42] = "苍老师是一个好人" ;
		
		
		List all = null ;
		long start = System.currentTimeMillis() ;
		int count = 0 ;
		for (int mm = 0; mm < 1; mm++) {
			for (int i = 0; i < strs.length; i++) {
				all = new ArrayList() ; 
				if(strs[i]==null)continue ;
//				Analysis udf = new UserDefinedAnalysis(new StringReader(strs[i]),true) ;
				Analysis udf = new ToAnalysis(new StringReader(strs[i])) ;
				Term term = null ;
				while((term=udf.next())!=null){
//					if(term.getTermNatures().personAttr.flag)
					all.add(term) ;
				}
				System.out.println("===========\t"+all);
//				System.out.println(NumRecognition.recognition(all));
			}
		}
		System.out.println(System.currentTimeMillis()-start);
		
	}
	
	
	
//	public static void main(String[] args) throws IOException {
//		String str = null ;
//		
////		BufferedReader br = IOUtil.getReader("C://Users//ansj//Desktop//新建文本文档.txt", "GBK") ;
//		String[] strs = new String[100] ;
//		strs[0] = "他说的确实在理" ;
//		strs[1] = "长春市长春节讲话" ;
//		strs[2] = "结婚的和尚未结婚的" ;
//		strs[3] = "结合成分子时" ;
//		strs[4] = "旅游和服务是最好的" ;
//		strs[5] = "邓颖超生前最喜欢的一个东西" ;
//		strs[6] = "中国航天官员应邀到美国与太空总署官员开会" ;
//		strs[7] = "上海大学城书店" ;
//		strs[8] = "北京大学生前来应聘" ;
//		strs[9] = "中外科学名著" ;
//		strs[10] = "为人民服务" ;
//		strs[11] = "独立自主和平等互利的原则" ;
//		strs[12] = "为人民办公益" ;
//		strs[13] = "这事的确定不下来" ;
//		strs[14] = "费孝通向人大常委会提交书面报告" ;
//		strs[15] = "aaa分事实上发货丨和无哦喝完酒" ;
//		strs[16] = "不好意思清清爽爽" ;
//		strs[17] = "长春市春节讲话" ;
//		strs[18] = "中华人民共和国万岁万岁万万岁" ;
//		strs[19] = "检察院鲍绍检察长" ;
//		strs[20] = "长春市长春药店" ;
//		strs[21] = "乒乓球拍卖完了" ;
//		strs[22] = "计算机网络管理员用虚拟机实现了手机游戏下载和开源项目的管理金山毒霸" ;	
//		strs[23] = "胡锦涛与神九航天员首次实现天地双向视频通话" ;
//		strs[23] = "mysql不支持 同台机器两个mysql数据库之间做触发器" ;
//		strs[24] = "孙建是一个好人.他和蔡晴是夫妻两 ,对于每一本好书他都原意一一读取..他们都很喜欢元宵.康燕和他们住在一起.我和马春亮,韩鹏飞都是好朋友" ;
//		strs[24] = "一年有三百六十五个日出 我送你三百六十五个祝福 时钟每天转了一千四百四十圈我的心每天都藏着 一千四百四十多个思念 每一天都要祝你快快乐乐  每一分钟都盼望你平平安安 吉祥的光永远环绕着你 像那旭日东升灿烂无比 " ;
//		strs[24] = "学校学费要一次性交一千元" ;
//		strs[24] = "发展中国家庭养猪事业" ;
//		strs[25] ="c语言经典书" ;
//		strs[26] ="c语言怎么写ini文件" ;
//		strs[27] ="c语言怎么读写ini文件" ;
//		strs[28] ="关卡编辑器" ;
//		strs[29] = "eclipse 多项目依赖" ;
//		strs[30] = "苍老师是一个好人" ;
//		
//		
//		List all = null ;
//		new TowWordLibrary() ;
//		long start = System.currentTimeMillis() ;
//		int count = 0 ;
//		for (int mm = 0; mm < 10000; mm++) {
//			for (int i = 0; i < strs.length; i++) {
////				all = new ArrayList() ; 
//				if(strs[i]==null)continue ;
//				count += strs[i].length() ;
////				Analysis udf = new UserDefinedAnalysis(new StringReader(strs[i]),true) ;
//				Analysis udf = new ToAnalysis(new StringReader(strs[i])) ;
//				Term term = null ;
//				while((term=udf.next())!=null){
////					all.add(term) ;
//				}
////				System.out.println(all);
////				System.out.println(NumRecognition.recognition(all));
//			}
//		}
//		System.out.println(System.currentTimeMillis()-start);
//		System.out.println("共分词:"+count+"个");
//		
//	}
}
