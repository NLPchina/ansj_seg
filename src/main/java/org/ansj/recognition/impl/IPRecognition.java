package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;

public class IPRecognition extends ExtractingRecognition {

	private static final long serialVersionUID = 1L;

	private static final TermNatures IP_T_N = new TermNatures(new TermNature("ip", 1));

	private static final Extracting EXTRACTING = new Extracting();

	private static final String REGEX_IPv4 = "(:m)[25[0-5]|][2[0-4]\\\\d|][1\\\\d{2}|][[0-9]\\\\d][\\\\d](\\.)" +
			"(:m)[25[0-5]|][2[0-4]\\\\d|][1\\\\d{2}|][[0-9]\\\\d][\\\\d](\\.)(:m)[25[0-5]|][2[0-4]\\\\d|][1\\\\d{2}|][[0-9]\\\\d][\\\\d](\\.)" +
			"(:m)[25[0-5]|][2[0-4]\\\\d|][1\\\\d{2}|][[0-9]\\\\d][\\\\d]";

	static {
		try {
			EXTRACTING.addRuleStr(REGEX_IPv4);
		} catch (RuleFormatException e) {
			e.printStackTrace();
		}
	}

	public IPRecognition() {
		super(EXTRACTING, IP_T_N);
	}
}
