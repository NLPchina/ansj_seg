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
					TermUtil.insertTermNum(terms, new Term(sb.toString(), begin, TermNatures.NB));
					sb = null;
				}
			}
		}

		if (sb != null) {
			TermUtil.insertTermNum(terms, new Term(sb.toString(), begin, TermNatures.NB));
		}
	}

	/**
	 * 数字+量词一个合并
	 */
	public static void recogntionNQ(Term[] terms) {
		Term term = null;
		Term to = null;

		for (int i = 0; i < terms.length - 1; i++) {
			term = terms[i];
			if (term != null && (to = terms[term.getToValue()]) != null) {
				if (term.getTermNatures().numAttr.flag && to.getTermNatures().numAttr.numEndFreq > -1) {
					terms[i] = TermUtil.makeNewTermNum(term, to, TermNatures.NB);
					terms[to.getOffe()] = null;
					i = to.getOffe();
				}

			}
		}
	}
}
