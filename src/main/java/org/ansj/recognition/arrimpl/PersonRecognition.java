package org.ansj.recognition.arrimpl;

import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.DATDictionary;
import org.ansj.recognition.TermArrRecognition;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil;

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

		Term[] terms = graph.terms;

		Term from = null, first = null, sencond = null, third = null, last = null, person = null;
		PersonNatureAttr fromPna ,fPna = null, sPna = null, tPna = null;
		double score = 0;

		for (int i = 0; i < terms.length; i++) {
			first = terms[i];
			fPna = getPersonNature(first);
			first.selfScore(0);
			if (!fPna.isActive()) {
				continue;
			}

			sencond = first.to();
			if (sencond.getOffe() == terms.length || sencond.getName().length() > 2) { //说明到结尾了,或者后面长度不符合规则
				continue;
			}
			sPna = getPersonNature(sencond);


			third = sencond.to();
			tPna = getPersonNature(third);

			from = first.from() ;
			fromPna = getPersonNature(from);

			//XD
			if (first.getName().length() == 2) {
				person = new Term(first.getName() + sencond.getName(), first.getOffe(), TermNatures.NR);
				score = fPna.getX() + sPna.getD();
				person.selfScore(score);
				TermUtil.insertTerm(terms, person, TermUtil.InsertTermType.REPLACE);
				continue;
			}

			//BE
			person = new Term(first.getName() + sencond.getName(), first.getOffe(), TermNatures.NR);
			score = fPna.getB() + sPna.getE();
			person.selfScore(score);
			TermUtil.insertTerm(terms, person, TermUtil.InsertTermType.REPLACE);


			if (third.getOffe() == terms.length || third.getName().length() > 1) { //说明到结尾了,或者后面长度不符合规则
				continue;
			}

			//BCD
			person = new Term(first.getName() + sencond.getName() + third.getName(), first.getOffe(), TermNatures.NR);
			score = fPna.getB() + sPna.getC() + tPna.getD();
			person.selfScore(score);
			TermUtil.insertTerm(terms, person, TermUtil.InsertTermType.REPLACE);

		}

		graph.walkPathByScore();

	}

	/**
	 * 获得一个term的personnature
	 *
	 * @param term
	 * @return
	 */
	private PersonNatureAttr getPersonNature(Term term) {
		PersonNatureAttr person = DATDictionary.person(term.getName());

		if(person==null){
			person =  DATDictionary.person(":"+term.getNatureStr());
		}

		if (person!=null) {
			return DATDictionary.person(term.getName());
		} else {
			return term.termNatures().personAttr;
		}
	}

}
