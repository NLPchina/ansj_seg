package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;

import java.util.Iterator;
import java.util.List;

/**
 * 基于规则的新词发现，身份证号码识别
 * 
 * @author ansj
 * 
 */
public class IDCardRecognition implements Recognition {
	/**
	 * 
	 */
	private static final long serialVersionUID = -32133440735240290L;
	private static final Nature ID_CARD_NATURE = new Nature("idcard");
	private static final String REGEX_ID_NO_18 = "\\d{6}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";

	@Override
	public void recognition(Result result) {

		List<Term> terms = result.getTerms() ;

		for (Term term : terms) {
			if ("m".equals(term.getNatureStr())) {

				if (term.getName().length() == 18 && term.getName().matches(REGEX_ID_NO_18)) {
					term.setNature(ID_CARD_NATURE);
				} else if (term.getName().length() == 17) {
					Term to = term.to();
					if ("x".equals(to.getName()) && (term.getName() + to.getName()).matches(REGEX_ID_NO_18)) {
						term.merage(to);
						to.setName(null);
						term.setNature(ID_CARD_NATURE);
					} else if (to.getName().startsWith("x") || to.getName().startsWith("X")) {
						String start = to.getName().substring(0, 1);
						if ((term.getName() + start).matches(REGEX_ID_NO_18)) {
							String substring = to.getName().substring(1);
							String real_substring = to.getRealName().substring(1);
							String real_start = to.getRealName().substring(0,1);
							term.setRealName(term.getRealName() + real_start);
							term.setName(term.getName() + start);
							to.setRealName(real_substring);
							to.setName(substring);
							term.setNature(ID_CARD_NATURE);
						}
					}
				}

			}
		}

		for (Iterator<Term> iterator = terms.iterator(); iterator.hasNext();) {
			Term term = iterator.next();
			if (term.getName() == null) {
				iterator.remove();
			}
		}

	}

}
