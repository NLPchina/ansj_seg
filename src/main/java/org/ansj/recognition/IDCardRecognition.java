package org.ansj.recognition;

import java.util.Iterator;
import java.util.List;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;

/**
 * 基于规则的新词发现，身份证号码识别
 * 
 * @author ansj
 * 
 */
public class IDCardRecognition {
	private static final Nature ID_CARD_NATURE = new Nature("idcard");

	public static List<Term> recognition(List<Term> terms) {

		for (Term term : terms) {
			if ("m".equals(term.getNatureStr())) {

				if (term.getName().length() == 18) {
					term.setNature(ID_CARD_NATURE);
				} else if (term.getName().length() == 17) {
					Term to = term.to();
					if ("x".equals(to.getName())) {
						term.merage(to);
						to.setName(null);
						term.setNature(ID_CARD_NATURE);
					}
				}

			}
		}

		for (Iterator<Term> iterator = terms.iterator(); iterator.hasNext();) {
			Term term = (Term) iterator.next();
			if (term.getName() == null) {
				iterator.remove();
			}
		}

		return terms;
	}

}
