package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.domain.ExtractingResult;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ansj on 09/10/2017.
 */
public class URLRecognition implements Recognition {
	private static final Extracting extracting = new Extracting();

	private static final TermNatures URL_T_N = new TermNatures(new TermNature("url", 1));


	static {
		try {
			extracting.addRuleStr("(http://|https://)(:en|:m|-|\\.)[[\\\\x00-\\\\xff]+]{1,100}");
		} catch (RuleFormatException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void recognition(Result result) {
		ExtractingResult parse = extracting.parse(result);


		for (List<Term> list : parse.findAll()) {

			String name = list.get(list.size() - 1).getName();

			while ("-".equals(name) || ".".equals(name)) {
				list.remove(list.size() - 1);
				name = list.get(list.size() - 1).getName();
			}

			if (list.size() == 1) {
				continue;
			}


			int beginOff = list.get(0).getOffe();

			int endOff = list.get(list.size() - 1).getOffe() + list.get(list.size() - 1).getName().length();

			List<Term> terms = result.getTerms();

			StringBuilder sb = new StringBuilder();

			StringBuilder sbReal = new StringBuilder();

			List<Term> newList = new ArrayList<>();

			for (int i = 0; i < terms.size(); i++) {
				Term term = terms.get(i);
				if (term.getOffe() >= beginOff && term.getOffe() < endOff) {
					sb.append(term.getName());
					if (term.getRealNameIfnull() != null) {
						sbReal.append(term.getRealName());
					}
				} else {
					if (sb != null && sb.length() > 0) {
						Term newTerm = new Term(sb.toString(), beginOff, URL_T_N);
						if (sbReal.length() > 0) {
							newTerm.setRealName(sbReal.toString());
						}
						newList.add(newTerm);
						sb = null;
						sbReal = null;
					}
					newList.add(term);

				}
			}
			result.setTerms(newList);
		}
	}


}
