package org.ansj.recognition.impl;

import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

/**
 * 同义词功能
 * 
 * @author Ansj
 *
 */
public class SynonymsRecgnition implements Recognition {

	private static final long serialVersionUID = 5961499108093950130L;

	private SmartForest<List<String>> synonyms = null;

	public SynonymsRecgnition() {
		this.synonyms = MyStaticValue.synonyms();
	}

	public SynonymsRecgnition(String key) {
		this.synonyms = MyStaticValue.synonyms(key);
	}


	@Override
	public void recognition(Result result) {
		for (Term term : result) {
			SmartForest<List<String>> branch = synonyms.getBranch(term.getName());
			if (branch != null && branch.getStatus() > 1) {
				List<String> syns = branch.getParam();
				if (syns != null) {
					term.setSynonyms(syns);
				}
			}
		}
	}

}
