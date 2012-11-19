package org.ansj.util.recognition;

import org.ansj.domain.CompanyNatureAttr;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.name.CompanyAttrLibrary;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil;

/**
 * 机构识别
 * 
 * @author ansj
 * 
 */
public class CompanyRecogntion {

	/**
	 * 2-n词概率
	 */
	private static final double[] FACTORY = CompanyAttrLibrary.loadFactory();

	private Term[] terms;

	public CompanyRecogntion(Term[] terms) {
		this.terms = terms;
	}

	private Term tempTerm = null;

	private int offe;
	private Term beginTerm;

	public void recogntion() {
		Term term = null;
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			if (term == null) {
				continue;
			}
			term.selfScore = 100;
			term.score = 0;
			if (term.getTermNatures().companyAttr.b > 100) {
				double tempScore = term.getTermNatures().companyAttr.bb;
				offe = term.getOffe();
				tempTerm = null;
				beginTerm = term.getFrom();
				recogntion(term, tempScore, 1 - tempScore);
				if (maxTerm != null) {
					TermUtil.insertTerm(terms, maxTerm);
					 System.out.println(maxTerm+"\t"+maxTerm.selfScore);
					maxTerm = null;
				}
			}
		}
	}

	private Term maxTerm = null;

	private void recogntion(Term term, double score1, double score2) {
		// TODO Auto-generated method stub
		String companyName = term.getName();
		CompanyNatureAttr companyAttr = null;

		while ((term = term.getTo()) != null && (companyAttr = term.getTermNatures().companyAttr) != CompanyNatureAttr.NULL) {

			companyName += term.getName();

			if (companyAttr.e > 50) {
				score1 *= term.getTermNatures().companyAttr.eb;
				score2 *= (1 - term.getTermNatures().companyAttr.eb);

				tempTerm = new Term(companyName, offe, TermNatures.NT);

				{

					tempTerm.selfScore = (score1 / (score1 + score2));
					if (tempTerm.selfScore > 0) {
						// 前缀分数
						if (beginTerm == null || beginTerm.getTermNatures() == TermNatures.BEGIN) {
							tempTerm.selfScore += CompanyNatureAttr.BEGIN;
						} else {
							tempTerm.selfScore += beginTerm.getTermNatures().companyAttr.pb;
						}

						// 后缀分数
						Term to = term.getTo();
						if (to == null || to.getTermNatures() == TermNatures.END) {
							tempTerm.selfScore += CompanyNatureAttr.END;
						} else {
							tempTerm.selfScore += to.getTermNatures().companyAttr.sb;
						}
						// 计算分数
						int length = companyName.length() > 50 ? 50 : companyName.length();
						tempTerm.selfScore += Math.log(FACTORY[length]);// *
																		// length;
						tempTerm.selfScore = 10 + tempTerm.selfScore;
						if (maxTerm == null || maxTerm.selfScore < tempTerm.selfScore) {
							maxTerm = tempTerm;
						}
					}
				}

				if (companyAttr.m > 50) {
					score1 *= term.getTermNatures().companyAttr.mb;
					score2 *= (1 - term.getTermNatures().companyAttr.mb);
				}

			}
		}
	}

	public static void main(String[] args) {
		System.out.println(Math.log(0.4));
		System.out.println(Math.log(0.2));
		System.out.println(Math.log(0.1));
	}
}
