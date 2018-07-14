package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.util.Graph;
import org.ansj.util.TermUtil;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 用户自定词典识别 ,对于结果后的二次处理
 *
 * @author Ansj
 *
 */
public class DicRecognition implements Recognition {

	private static final long serialVersionUID = 7487741700410080896L;

	private Forest[] forests;

	private TermUtil.InsertTermType type = TermUtil.InsertTermType.REPLACE ;

	public DicRecognition(Forest... forests) {
		this.forests = forests;
	}

	public DicRecognition(TermUtil.InsertTermType type ,Forest... forests) {
		this.type = type ;
		this.forests = forests;
	}


	@Override
	public void recognition(Result result) {
		Graph graph = new Graph(result);
		new UserDefineRecognition(type, forests).recognition(graph);
		graph.rmLittlePath();
		graph.walkPathByScore();

		List<Term> terms = new ArrayList<Term>();
		int length = graph.terms.length - 1;
		for (int i = 0; i < length; i++) {
			if (graph.terms[i] != null) {
				terms.add(graph.terms[i]);
			}
		}
		result.setTerms(terms);
	}

}
