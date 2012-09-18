package org.ansj.util.recognition;

import org.ansj.domain.NumNatureAttr;
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
		int length = terms.length - 1;
		int begin = -1;
		Term from = null;
		Term to = null;
		for (int i = 0; i < length; i++) {
			if (terms[i] == null) {
				continue;
			}
			// 发现数字进行模式匹配找到numEndFreq
			if (terms[i].getTermNatures().numAttr.flag) {
				from = terms[i].getFrom();
				sb = new StringBuilder();
				begin = i;
				do {
					if (terms[i] == null) {
						i++;
						continue;
					} else {
						to = terms[i].getTo();
					}
					sb.append(terms[i].getName());
					terms[i] = null;
					i++;
				} while (i < length && (terms[i] == null || terms[i].getTermNatures().numAttr.flag));

				//合并最后的量词
				if (i < length && terms[i].getTermNatures().numAttr.numEndFreq > 0) {
					sb.append(terms[i].getName());
					to = terms[i].getTo() ;
					terms[i] = null;
				}

				if(sb.length()>1){
					Term term = new Term(sb.toString(), begin, TermNatures.NB);
					TermUtil.termLink(from, term);
					TermUtil.termLink(term, to);
					TermUtil.insertTermNum(terms, term);
				}
			}
		}

	}

	/**
	 * 数字+量词一个合并
	 */
	// public static void recogntionNQ(Term[] terms) {
	// Term term = null;
	// Term to = null;
	//
	// for (int i = 0; i < terms.length - 1; i++) {
	// term = terms[i];
	// if (term != null && (to = terms[term.getToValue()]) != null) {
	// if (term.getTermNatures().numAttr.flag &&
	// to.getTermNatures().numAttr.numEndFreq > -1) {
	// terms[i] = TermUtil.makeNewTermNum(term, to, TermNatures.NB);
	// terms[to.getOffe()] = null;
	// i = to.getOffe();
	// }
	//
	// }
	// }
	// }
}
