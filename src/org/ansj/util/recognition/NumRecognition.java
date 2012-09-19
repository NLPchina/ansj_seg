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
		int length = terms.length - 1;
		int begin = -1;
		Term from = null;
		Term to = null;
		Term temp = null;
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
					temp = terms[i];
					terms[i] = null;
					i++;
				} while (i < length && (terms[i] == null || terms[i].getTermNatures().numAttr.flag));

				// 合并最后的量词
				if (i < length && terms[i].getTermNatures().numAttr.numEndFreq > 0) {
					sb.append(terms[i].getName());
					to = terms[i].getTo();
					terms[i] = null;
				}

				if (sb.length() > 1) {
					Term term = new Term(sb.toString(), begin, TermNatures.NB);
					TermUtil.termLink(from, term);
					TermUtil.termLink(term, to);
					TermUtil.insertTermNum(terms, term);
				} else {
					terms[temp.getOffe()] = temp;
				}
			}
		}

	}
}
