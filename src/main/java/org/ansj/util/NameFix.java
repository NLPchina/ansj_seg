package org.ansj.util;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;

public class NameFix {
	/**
	 * 人名消歧,比如.邓颖超生前->邓颖 超生 前 fix to 丁颖超 生 前! 规则的方式增加如果两个人名之间连接是- ， ·，•则连接
	 */
	public static void nameAmbiguity(Term[] terms) {
		Term from = null;
		Term term = null;
		Term next = null;
		for (int i = 0; i < terms.length - 1; i++) {
			term = terms[i];
			if (term != null && term.getTermNatures() == TermNatures.NR && term.getName().length() == 2) {
				next = terms[i + 2];
				if (next.getTermNatures().personAttr.split > 0) {
					term.setName(term.getName() + next.getName().charAt(0));
					terms[i + 2] = null;
					terms[i + 3] = new Term(next.getName().substring(1), next.getOffe(), TermNatures.NW);
					TermUtil.termLink(term, terms[i + 3]);
					TermUtil.termLink(terms[i + 3], next.getTo());
				}
			}
		}

		// 外国人名修正
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			if (term != null && term.getName().length() == 1 && i > 0 && WordAlert.CharCover(term.getName().charAt(0)) == '·') {
				from = term.getFrom();
				next = term.getTo();

				if (from.getNature().natureStr.startsWith("nr") && next.getNature().natureStr.startsWith("nr")) {
					from.setName(from.getName() + term.getName() + next.getName());
					TermUtil.termLink(from, next.getTo());
					terms[i] = null;
					terms[i + 1] = null;
				}
			}
		}
	}
}
