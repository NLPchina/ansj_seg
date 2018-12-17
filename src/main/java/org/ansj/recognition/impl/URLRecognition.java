package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;

/**
 * Created by Ansj on 09/10/2017.
 */
public class URLRecognition extends ExtractingRecognition {

	private static final long serialVersionUID = 1L;
	
	private static final Extracting EXTRACTING = new Extracting();
	private static final TermNatures URL_T_N = new TermNatures(new TermNature("url", 1));

	static {
		try {
			EXTRACTING.addRuleStr("(http://|https://|ftp://)(:en|:m|:mq|-|\\.|/|?|%|=|&)[[\\\\x00-\\\\xff]+]{1,1000}");
		} catch (RuleFormatException e) {
			e.printStackTrace();
		}
	}

	public URLRecognition() {
		super(EXTRACTING, URL_T_N);
	}
}
