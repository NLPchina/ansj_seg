package org.ansj.recognition.arrimpl;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.TermArrRecognition;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil;
import org.nlpcn.commons.lang.viterbi.Viterbi;

/**
 * 人名识别工具类
 *
 * @author ansj
 */
public class PersonRecognition implements TermArrRecognition {

	private static final String E = "末##末";

	//BE BCD UD XD

	private static final double[] FACTORY = {0.16271366224044456, 0.8060521860870434, 0.031234151672511947};

	@Override
	public void recognition(Graph graph) {

		Term[] terms = graph.terms ;

		Term first = null;
		Term to = null;
		Term toto = null;
		Term person = null;
		double score = 0;

		for (int i = 0; i < terms.length; i++) {
			first = terms[i];

			first.selfScore(0);

			if (!first.termNatures().personAttr.isActive()) {
				continue;
			}

			to = first.to();
			if (to.getOffe() == terms.length || to.getName().length() > 2) { //说明到结尾了,或者后面长度不符合规则
				continue;
			}

			//XD
			if (first.getName().length() == 2) {
				person = new Term(first.getName() + to.getName(), first.getOffe(), TermNatures.NR);
				score = first.termNatures().personAttr.getX() + to.termNatures().personAttr.getD();
				person.selfScore(score);
				TermUtil.insertTerm(terms, person, TermUtil.InsertTermType.REPLACE);
				continue;
			}

			//BE
			person = new Term(first.getName() + to.getName(), first.getOffe(), TermNatures.NR);
			score = first.termNatures().personAttr.getB() + to.termNatures().personAttr.getE();
			person.selfScore(score);
			TermUtil.insertTerm(terms, person, TermUtil.InsertTermType.REPLACE);

			//BCD
			toto = to.to();
			if (toto.getOffe() == terms.length || toto.getName().length() > 1) { //说明到结尾了,或者后面长度不符合规则
				continue;
			}
			person = new Term(first.getName() + to.getName() + toto.getName(), first.getOffe(), TermNatures.NR);
			score = first.termNatures().personAttr.getB() + to.termNatures().personAttr.getC() + toto.termNatures().personAttr.getD();
			person.selfScore(score);
			TermUtil.insertTerm(terms, person, TermUtil.InsertTermType.REPLACE);

		}

		graph.walkPathByScore();


	}

}
