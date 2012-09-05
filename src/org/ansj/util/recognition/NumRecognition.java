package org.ansj.util.recognition;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.util.TermUtil;

public class NumRecognition {
	
	/**
	 * 数字+数字合并
	 * 
	 * @param terms
	 */
	public static void recogntionNM(Term[] terms) {
		StringBuilder sb = null;
		int begin = 0;
		for (int i = 0; i < terms.length; i++) {
			if (terms[i] != null && terms[i].getTermNatures().numAttr.flag && terms[i].getNext() == null) {
				if (sb == null) {
					sb = new StringBuilder(terms[i].getName());
					begin = i;
				} else {
					sb.append(terms[i].getName());
					terms[i] = null;
				}
			} else {
				if (sb != null) {
					TermUtil.insertTerm(terms, new Term(sb.toString(), begin, TermNatures.NB));
					sb = null;
				}
			}
		}

		if (sb != null) {
			TermUtil.insertTerm(terms, new Term(sb.toString(), begin, TermNatures.NB));
		}
	}

	/**
	 * 数字+量词一个合并
	 */
	public static void recogntionNQ(Term[] terms) {
		Term term = null;

		for (int i = 0; i < terms.length - 1; i++) {
			term = terms[i];
			if (term != null && term.getTo() != null) {
				if (term.getTermNatures().numAttr.flag && term.getTo().getTermNatures().numAttr.numEndFreq > -1) {
					terms[i] = TermUtil.makeNewTermNum(term, term.getTo(), TermNatures.NB);
					terms[term.getTo().getOffe()] = null;
					i = term.getToValue();
				}

			}
		}
	}
}
