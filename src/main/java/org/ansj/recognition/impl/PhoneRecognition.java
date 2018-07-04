package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneRecognition implements Recognition {

	private static final long serialVersionUID = -32133440735240290L;

	private static final Nature PHONE_NATURE = new Nature("phone");

	@Override
	public void recognition(Result result) {

		List<Term> terms = result.getTerms();

		for (Term term : terms) {
			if ("m".equals(term.getNatureStr())) {

				if (term.getName().length() == 11) {
					String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
					Pattern p = Pattern.compile(regExp);
					Matcher m = p.matcher(term.getName());
					if (m.matches()) {
						term.setNature(PHONE_NATURE);
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
