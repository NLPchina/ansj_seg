package org.ansj.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

public class Test {

	public static void main(String[] args) throws IOException {
		HashSet<String> all = new HashSet();
		all.add("他说的确实在理");
		all.add("长春市长春节讲话");
		all.add("结婚的和尚未结婚的");
		all.add("结合成分子时");
		all.add("旅游和服务是最好的");
		all.add("邓颖超生前最喜欢的一个东西");
		all.add("中国航天官员应邀到美国与太空总署官员开会");
		all.add("上海大学城书店");
		all.add("北京大学生前来应聘");
		all.add("中外科学名著");
		all.add("为人民服务");
		all.add("独立自主和平等互利的原则");
		all.add("为人民办公益");
		all.add("这事的确定不下来");
		all.add("费孝通向人大常委会提交书面报告");
		all.add("aaa分事实上发货丨和无哦喝完酒");
		all.add("不好意思清清爽爽");
		all.add("长春市春节讲话");
		all.add("中华人民共和国万岁万岁万万岁");
		all.add("检察院鲍绍检察长就是在世诸葛.像诸葛亮一样聪明");
		all.add("长春市长春药店");
		all.add("乒乓球拍卖完了");
		all.add("计算机网络管理员用虚拟机实现了手机游戏下载和开源项目的管理金山毒霸");
		all.add("长春市长春药店");
		all.add("胡锦涛与神九航天员首次实现天地双向视频通话");
		all.add("mysql不支持 同台机器两个mysql数据库之间做触发器");
		all.add("孙建是一个好人.他和蔡晴是夫妻两 ,对于每一本好书他都原意一一读取..他们都很喜欢元宵.康燕和他们住在一起.我和马春亮,韩鹏飞都是好朋友,不知道什么原因");
		all.add("一年有三百六十五个日出 我送你三百六十五个祝福 时钟每天转了一千四百四十圈我的心每天都藏着 一千四百四十多个思念 每一天都要祝你快快乐乐  每一分钟都盼望你平平安安 吉祥的光永远环绕着你 像那旭日东升灿烂无比 ");
		all.add(" 一年有三百六十五个日出");
		all.add("学校学费要一次性交一千元");
		all.add("发展中国家庭养猪事业");
		all.add("安徽省是一个发展中的省");
		all.add("北京理工大学办事处");
		all.add("上海大学城");
		all.add("脚下的一大块方砖地面");
		all.add("程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪");
		all.add("中文分词 是一个实现,中文分词是一个实现");
		all.add("吃葡萄牙酸");
		all.add("葡萄牙的进攻");
		all.add("凭医生处方才可购买");
		all.add("工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作");
		all.add("门把手坏了,门把手夹了");
		all.add("一个和尚挑水喝,两个和尚抬水喝,三个和尚无水喝 , 一次性交易就毁了施水才");
		all.add("老乡们决定把他重新点燃起来");
		all.add("我的同事张三和李四是夫妻");
		all.add("下面就为大家整理了央视主持人的帅仔 靓女曝光照");
		all.add("两毛五一斤.一斤八两");
		all.add("在训练中将知识巩固");
		all.add("中将孙建很厉害");
		all.add("审讯室里一直陪着我们的两个警察");
		all.add("一只胳膊两个警察");
		all.add("c语言怎么读写ini文件");
		all.add("关卡编辑器");
		all.add("eclipse 多项目依赖");
		all.add("张媛:猩猩的娃叫陈美希吗？不知为什么让我突然想起了林明美");
		all.add("李建民工作了一天");
		all.add("李民工作了一天");
		all.add("李民工作了爸爸");
		all.add("井冈山：党建信息化服务新平台");
		all.add("如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常。如果您认为不应该使用代理服务器，请调整您的代理设置：转至扳手菜单 > 设置 > 显示高级设置... > 更改代理服务器设置... > LAN 设置，取消选中“为 LAN 使用代理服务器”复选框");
		all.add("在业内闻名");
		all.add("这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。");
		all.add("我不喜欢日本和服。");
		all.add("雷猴回归人间。");
		all.add("工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作");
		all.add("我需要廉租房");
		all.add("永和服装饰品有限公司");
		all.add("我爱北京天安门");
		all.add("abc");
		all.add("隐马尔可夫");
		all.add("雷猴是个好网站");
		all.add("“Microsoft”一词由“MICROcomputer（微型计算机）”和“SOFTware（软件）”两部分组成");
		all.add("草泥马和欺实马是今年的流行词汇");
		all.add("伊藤洋华堂总府店");
		all.add("中国科学院计算技术研究所");
		all.add("罗密欧与朱丽叶");
		all.add("我购买了道具和服装");
		all.add("PS: 我觉得开源有一个好处，就是能够敦促自己不断改进，避免敞帚自珍");
		all.add("湖北省石首市");
		all.add("湖北省十堰市");
		all.add("总经理完成了这件事情");
		all.add("电脑修好了");
		all.add("做好了这件事情就一了百了了");
		all.add("人们审美的观点是不同的");
		all.add("我们买了一个美的空调");
		all.add("线程初始化时我们要注意");
		all.add("一个分子是由好多原子组织成的");
		all.add("祝你马到功成");
		all.add("他掉进了无底洞里");
		all.add("中国的首都是北京");
		all.add("孙君意");
		all.add("外交部发言人马朝旭");
		all.add("领导人会议和第四届东亚峰会");
		all.add("在过去的这五年");
		all.add("还需要很长的路要走");
		all.add("60周年首都阅兵");
		all.add("你好人们审美的观点是不同的");
		all.add("买水果然后来世博园");
		all.add("买水果然后去世博园");
		all.add("但是后来我才知道你是对的");
		all.add("存在即合理");
		all.add("的的的的的在的的的的就以和和和");
		all.add("I love你，不以为耻，反以为rong");
		all.add("因");
		all.add("");
		all.add("hello你好人们审美的观点是不同的");
		all.add("很好但主要是基于网页形式");
		all.add("hello你好人们审美的观点是不同的");
		all.add("为什么我不能拥有想要的生活");
		all.add("后来我才");
		all.add("此次来中国是为了");
		all.add("使用了它就可以解决一些问题");
		all.add(",使用了它就可以解决一些问题");
		all.add("其实使用了它就可以解决一些问题");
		all.add("好人使用了它就可以解决一些问题");
		all.add("是因为和国家");
		all.add("老年搜索还支持");
		all.add("干脆就把那部蒙人的闲法给废了拉倒！RT @laoshipukong : 27日，全国人大常委会第三次审议侵权责任法草案，删除了有关医疗损害责任“举证倒置”的规定。在医患纠纷中本已处于弱势地位的消费者由此将陷入万劫不复的境地。 ");
		all.add("大");
		all.add("");
		all.add("他说的确实在理");
		all.add("长春市长春节讲话");
		all.add("结婚的和尚未结婚的");
		all.add("结合成分子时");
		all.add("旅游和服务是最好的");
		all.add("这件事情的确是我的错");
		all.add("供大家参考指正");
		all.add("哈尔滨政府公布塌桥原因");
		all.add("我在机场入口处");
		all.add("邢永臣摄影报道");
		all.add("BP神经网络如何训练才能在分类时增加区分度？");
		all.add("南京市长江大桥");
		all.add("应一些使用者的建议，也为了便于利用NiuTrans用于SMT研究");
		all.add("长春市长春药店");
		all.add("邓颖超生前最喜欢的衣服");
		all.add("胡锦涛是热爱世界和平的政治局常委");
		all.add("程序员祝海林和朱会震是在孙健的左面和右面, 范凯在最右面.再往左是李松洪");
		all.add("一次性交多少钱");
		all.add("两块五一套，三块八一斤，四块七一本，五块六一条");
		all.add("小和尚留了一个像大和尚一样的和尚头");
		all.add("我是中华人民共和国公民;我爸爸是共和党党员; 地铁和平门站");
		all.add("二次元乳量，养眼美女，我在泰国用微信");
		ToAnalysis.paser("123孙健234你好公司 有限!");
		long start = System.currentTimeMillis();
		int count = 0;
		for (int mm = 0; mm < 1; mm++) {
			for (String string : all) {
				count += string.length();
				List list = new ArrayList();
				ToAnalysis udf = new ToAnalysis(new StringReader(string));
				Term term = null;
				while ((term = udf.next()) != null) {
					list.add(term);
				}
				new NatureRecognition(list).recognition() ;
				System.out.println(list);
			}
		}
		System.out.println(System.currentTimeMillis() - start);

//		System.out.println(count / ((System.currentTimeMillis() - start) / 1000));

		// all.add("程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪");
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
