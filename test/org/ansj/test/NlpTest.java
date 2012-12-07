package org.ansj.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.newWordFind.LearnTool;

public class NlpTest {
	public static void main(String[] args) throws IOException {
		
		List<String> value = new ArrayList<String>() ;
		value.add("屌丝男士》是搜狐视频自制节目《大鹏嘚吧嘚》除“大鹏剧场秀“之外的第二个衍生品牌,是独立于《大鹏嘚吧嘚》每周播出的迷你剧集,第一季于2012年10月10日首播,每周三更新.该片由赵本山第53位弟子,网络第一主持人大鹏(董成鹏)导演并主演,是一部向德国电视剧《屌丝女士》致敬的喜剧短片.大鹏在片中饰演现实生活中的各种男性,而大鹏的各位明星好友,也在片中有惊喜表演.第一季客串明星:柳岩,李响,刘心,何云伟,李菁,如花李健仁,李亚红,乔衫,修睿,赵铭,于莎莎,司马南,不加V,沈腾等") ;
		value.add("二次元乳量大不一定是王道") ;
		value.add("在泰国用微信一搜吓尿了") ;
		
		//学习机器是有状态的
		long start = System.currentTimeMillis() ;
		LearnTool learn = new LearnTool() ;
		
		for (String string : value) {
			List<Term> paser = NlpAnalysis.paser(string, learn) ;
			System.out.println(paser);
		}
		
		System.out.println("这次训练已经学到了: "+learn.count+" 个词!");
		System.out.println(System.currentTimeMillis()-start);
		System.out.println(learn.getTopTree(100));
	}
}
