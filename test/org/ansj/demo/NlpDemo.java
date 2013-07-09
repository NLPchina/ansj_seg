package org.ansj.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class NlpDemo {
	public static void main(String[] args) throws IOException {
		
		List<String> value = new ArrayList<String>() ;
//		value.add("屌丝男士》是搜狐视频自制节目《大鹏嘚吧嘚》除“大鹏剧场秀“之外的第二个衍生品牌,是独立于《大鹏嘚吧嘚》每周播出的迷你剧集,第一季于2012年10月10日首播,每周三更新.该片由赵本山第53位弟子,网络第一主持人大鹏(董成鹏)导演并主演,是一部向德国电视剧《屌丝女士》致敬的喜剧短片.大鹏在片中饰演现实生活中的各种男性,而大鹏的各位明星好友,也在片中有惊喜表演.第一季客串明星:柳岩,李响,刘心,何云伟,李菁,如花李健仁,李亚红,乔衫,修睿,赵铭,于莎莎,司马南,不加V,沈腾等") ;
//		value.add("二次元乳量大不一定是王道") ;
//		value.add("在泰国用微信一搜吓尿了孙健和孙健是好朋友") ;
//		value.add("搜索日志是理解互联网用户信息的宝贵资源。本文基于搜索日志的特点，提出一种双层识别模型方法识别计算机领域查询串。第一层模型采用贝叶斯模型基于领域词库对查询串进行识别，可以达到较高的准确率，由于日志中查询串长度有限，信息量不足等特点导致一些查询串无法召回；针对如上情况我们提出补充信息维度，即在此基础上对其进行第二层模型训练，主要方法是依据查询串点击的URL信息进行可信度训练，依据查询串召回的URL信息进行行业可信度训练计算，以达到召回了更多计算机领域查询串的目的。实验结果表明，双层模型识别后结果不但在准确率上得到保障，并比第一层模型的召回率提高了20个百分点，达到了78%的召回率和96%的精准率。此方法迅速而准确的识别出计算机类别查询串，对其他领域查询识别及查询意图分类具有借鉴意义。") ;
//		value.add("贾瑞听了，魂不附体，只说：“好侄儿，只说没有见我，明日我重重的谢你。”贾蔷道：“你若谢我，放你不值什么，只不知你谢我多少？况且口说无凭，写一文契来。”贾瑞道：“这如何落纸呢？\"贾蔷道：“这也不妨，写一个赌钱输了外人帐目，借头家银若干两便罢。”贾瑞道：“这也容易．只是此时无纸笔。”贾蔷道：“这也容易。”说罢翻身出来，纸笔现成，拿来命贾瑞写．他两作好作歹，只写了五十两，然后画了押，贾蔷收起来．然后撕逻贾蓉．贾蓉先咬定牙不依，只说：“明日告诉族中的人评评理。”贾瑞急的至于叩头．贾蔷作好作歹的，也写了一张五十两欠契才罢．贾蔷又道：“如今要放你，我就担着不是．老太太那边的门早已关了，老爷正在厅上看南京的东西，那一条路定难过去，如今只好走后门．若这一走，倘或遇见了人，连我也完了．等我们先去哨探哨探，再来领你．这屋你还藏不得，少时就来堆东西．等我寻个地方。”说毕，拉着贾瑞，仍熄了灯，出至院外，摸着大台矶底下，说道：“这窝儿里好，你只蹲着，别哼一声，等我们来再动。”说毕，二人去了") ;
//		value.add("接了个小私活，帮一个初中高中连读的中学做一个学生日常考评系统，就是记录迟到、早退、违纪什么的一个系统，由班主任管理记录，还要有什么表扬榜的。对于报价不了解，不知道该报多少，大家说说看，多少合适？") ;
		value.add("若雅虎关闭了,我就不访问网站了!") ;
		
		//学习机器是有状态的
		long start = System.currentTimeMillis() ;
		//此对象可以公用一个.随着语料的增多可以学习新的词语
		LearnTool learn = new LearnTool() ;
		
		//关闭人名识别
		learn.isAsianName = false ;
		//关闭机构名识别
		learn.isCompany= false ;
		//关闭外国人名识别
		learn.isForeignName=false ;
		//关闭新词发现
		learn.isNewWord = false ;
		
		for (String string : value) {
			List<Term> parse = NlpAnalysis.parse(string, learn) ;
			System.out.println(parse);
		}
		
		System.out.println("这次训练已经学到了: "+learn.count+" 个词!");
		System.out.println(System.currentTimeMillis()-start);
		System.out.println(learn.getTopTree(100));
	}
}
