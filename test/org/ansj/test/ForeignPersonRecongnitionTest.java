package org.ansj.test;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class ForeignPersonRecongnitionTest {
	public static void main(String[] args) {
		List<Term> parse = ToAnalysis.parse("俞志龙和陈举亚是南京维数公司的同事 ,保护协会，协会主席亚拉·巴洛斯说他们是在1990年开始寻找野生金刚鹦鹉的，最后终于找到了唯一的一只，是一只雄性鹦鹉，从那以后生物学家一直在观察它，因为再没有发现第二只野生的金刚鹦鹉。巴洛斯说") ;
		new NatureRecognition(parse).recognition() ;
		System.out.println(parse);
	}
}
