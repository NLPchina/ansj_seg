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

	// private static final char[] open = {'(','（'} ;
	// private static final char[] close = {')','）'} ;
	//
	// private boolean isOpen ;

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
			term.selfScore = 0;
			term.score = 0;
			if (term.getName().equals("东华")) {
				System.out.println(12);
			}
			if (term.getTermNatures().companyAttr.bb < -0.005 && term.getTermNatures().companyAttr.b > 1000) {
				System.out.println("----" + term + "\t" + term.getTermNatures().companyAttr.bb);
				double tempScore = term.getTermNatures().companyAttr.bb;
				offe = term.getOffe();
				tempTerm = null;
				beginTerm = term.getFrom();
				recogntion(term, tempScore);
				if (maxTerm != null) {
					TermUtil.insertTerm(terms, maxTerm);
					System.out.println(maxTerm + "\t" + maxTerm.selfScore);
					maxTerm = null;
				}
			}
		}
	}

	private Term maxTerm = null;

	private void recogntion(Term term, double score) {
		// TODO Auto-generated method stub
		String companyName = term.getName();
		CompanyNatureAttr companyAttr = null;
		while ((term = term.getTo()) != null && (companyAttr = term.getTermNatures().companyAttr) != CompanyNatureAttr.NULL) {

			companyName += term.getName();
			if (companyAttr.eb < -0.005 && companyAttr.e > 200) {
				score += term.getTermNatures().companyAttr.eb;

				tempTerm = new Term(companyName, offe, TermNatures.NT);

				tempTerm.selfScore = score;
				// 前缀分数
				if (beginTerm == null || beginTerm.getTermNatures() == TermNatures.BEGIN) {
					System.out.println(tempTerm + "\t" + beginTerm + "\t" + beginTerm.getTermNatures().companyAttr.sb);
					tempTerm.selfScore += beginTerm.getTermNatures().companyAttr.pb;
				}

				// 后缀分数
				Term to = term.getTo();
				if (to == null || to.getTermNatures() == TermNatures.END) {
					System.out.println(tempTerm + "\t" + to + "\t" + to.getTermNatures().companyAttr.sb);
					tempTerm.selfScore += to.getTermNatures().companyAttr.sb;
				}
				// 计算分数
				int length = companyName.length() > 50 ? 50 : companyName.length();
				tempTerm.selfScore *= -Math.log(1 - FACTORY[length]);// *

				System.out.println(tempTerm + "\t" + tempTerm.selfScore);
				// length;
				if (maxTerm == null || maxTerm.selfScore > tempTerm.selfScore) {
					maxTerm = tempTerm;
				}
			}

			if (companyAttr.mb < -0.005 && companyAttr.m > 50) {
				score += term.getTermNatures().companyAttr.mb;
			} else {
				return;
			}
		}
	}
}
