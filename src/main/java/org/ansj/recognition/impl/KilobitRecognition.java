package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;

/**
 * Created by Ansj on 24/11/2017.
 * 千位分隔符的数字，如 12,345.60元  1，232
 */
public class KilobitRecognition extends ExtractingRecognition {

	private static final long serialVersionUID = 1L;

	private static final TermNatures KILOBIT_T_N = new TermNatures(new TermNature("kilobit", 1));

	private static final Extracting EXTRACTING = new Extracting();

	static {
		try {
			EXTRACTING.addRuleStr("(:m)[\\\\d+][[a-zA-Z]+][\\\\.][-]{1,50}(@)(:m|:en|.|-)[\\\\d+][[a-zA-Z]+][\\\\.][-]{1,50}");
		} catch (RuleFormatException e) {
			e.printStackTrace();
		}
	}

	public KilobitRecognition() {
		super(EXTRACTING, KILOBIT_T_N);
	}
}
