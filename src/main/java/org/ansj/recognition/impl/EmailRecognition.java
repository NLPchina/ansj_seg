package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;

/**
 * 电子邮箱抽取
 *
 * @author ansj
 */
public class EmailRecognition extends ExtractingRecognition {

	private static final long serialVersionUID = 1L;

	private static final TermNatures EMAIL_T_N = new TermNatures(new TermNature("email", 1));

	private static final Extracting EXTRACTING = new Extracting();

	static {
		try {
			EXTRACTING.addRuleStr("(:m|:en|.|-)[\\\\d+][[a-zA-Z]+][\\\\.][-]{1,50}(@)(:m|:en|.|-)[\\\\d+][[a-zA-Z]+][\\\\.][-]{1,50}");
		} catch (RuleFormatException e) {
			e.printStackTrace();
		}
	}

	public EmailRecognition() {
		super(EXTRACTING, EMAIL_T_N);
	}
}
