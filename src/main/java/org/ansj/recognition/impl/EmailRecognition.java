package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

/**
 * 电子邮箱抽取
 *
 * @author ansj
 */
public class EmailRecognition implements Recognition {

	private static final Log LOG = LogFactory.getLog();

	private static final Extracting EXTRACTING = new Extracting();

	static {
		try {
			EXTRACTING.addRuleStr("(:m|:en|.|-)[\\\\d+][[a-zA-Z]+][\\\\.][-]{1,50}(@)(:m|:en|.|-)[\\\\d+][[a-zA-Z]+][\\\\.][-]{1,50}\temail:0,1,2");
		} catch (RuleFormatException e) {
			e.printStackTrace();
			LOG.error("email recognition err ", e);
		}

	}

	public static void main(String[] args) {
		//我的邮箱是ansj-sun@163.com
		System.out.println(EXTRACTING.parse("我的qq邮箱是5144694@qq.com").getAllResult());

	}

	@Override
	public void recognition(Result result) {

		List<Term> terms = result.getTerms();

		for (Term term : terms) {
			if (!"@".equals(term.getName())) {
				continue;
			}

		}

		for (Iterator<Term> iterator = terms.iterator(); iterator.hasNext(); ) {
			Term term = iterator.next();
			if (term.getName() == null) {
				iterator.remove();
			}
		}

	}
}
