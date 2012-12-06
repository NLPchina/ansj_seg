package org.ansj.util.recognition;

import love.cq.domain.SmartForest;

import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.util.TermUtil;
import org.ansj.util.newWordFind.LearnTool;

/**
 * 新词识别
 * 
 * @author ansj
 * 
 */
public class NewWordRecognition {

	private Term[] terms = null;

	private SmartForest<NewWord> forest = null;

	private SmartForest<NewWord> branch = null;

	private int offe = -1;
	private int endOffe = -1;
	private TermNatures tempNatures;

	public NewWordRecognition(Term[] terms, LearnTool learn) {
		this.terms = terms;
		forest = learn.getForest();
		branch = learn.getForest();
	}

	public void recognition() {
		if (branch == null) {
			return;
		}
		int length = terms.length - 1;

		for (int i = 0; i < length; i++) {
			if (terms[i] == null)
				continue;
			branch = branch.getBranch(terms[i].getName());
			if (branch == null) {
				if (offe != -1 && offe < endOffe) {
					i = offe;
					makeNewTerm();
				} else {
					if (offe != -1) {
						i = offe;
					}
					reset();
				}
			} else if (branch.getStatus() == 3) {
				endOffe = i;
				tempNatures = branch.getParam().getNature();
				if (offe != -1 && offe < endOffe) {
					i = offe;
					makeNewTerm();
				} else {
					reset();
				}
			} else if (branch.getStatus() == 2) {
				endOffe = i;
				if (offe == -1) {
					offe = i;
				} else {
					tempNatures = branch.getParam().getNature();
				}
			} else if (branch.getStatus() == 1) {
				if (offe == -1) {
					offe = i;
				}
			}
		}

		if (offe != -1 && offe < endOffe) {
			makeNewTerm();
		}

	}

	private void makeNewTerm() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (int j = offe; j <= endOffe; j++) {
			if (terms[j] == null) {
				continue;
			} else {
				sb.append(terms[j].getName());
			}
			// terms[j] = null;
		}

		Term term = new Term(sb.toString(), offe, tempNatures);
		TermUtil.insertTerm(terms, term);
		reset();
	}

	/**
	 * 重置
	 */
	private void reset() {
		offe = -1;
		endOffe = -1;
		tempNatures = null;
		branch = forest;
	}

}
