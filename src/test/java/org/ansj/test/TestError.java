package org.ansj.test;

import java.util.List;

import junit.framework.Assert;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		String str = "里奇和波什都因伤退出，内线又一次成为美国男篮的问题，以目前球队的大名单，两大内线钱德勒和勒夫出战奥运几乎可以确定。科朗吉洛接受福克斯体育的电话采访时明确表示奥运阵容已确认9人。2008年北京奥运夺冠的科比、詹姆斯、安东尼、保罗和德隆肯定亮相伦敦，2010年世锦赛上帮助球队夺冠的杜兰特、威斯布鲁克、勒夫和钱德勒也会入选奥运名单。" ;
		int len = 0 ;
		LearnTool tool = new LearnTool() ;
		List<Term> parse = NlpAnalysis.parse(str,tool);
		System.out.println(parse);
		for (Term term : parse) {
			len += term.getName().length() ;
		}
		System.out.println(tool.getTopTree(100));; 
		Assert.assertEquals(len, str.length()); 
	}
}
