package org.ansj.splitWord.impl;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class ForeignPersonRecongnitionTest {
	public static void main(String[] args) {
		List<Term> paser = ToAnalysis.paser("马克·扎克伯格亮相了周二 TechCrunch Disrupt 大会，并针对公司不断下挫的股价、移动战略、广告业务等方面发表了讲话。自 5 月公司 IPO 后，扎克伯格极少公开露面，这也是他首次在重要场合公开接受采访") ;
		System.out.println(paser);
	}
}
