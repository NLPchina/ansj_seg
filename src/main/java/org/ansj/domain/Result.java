package org.ansj.domain;

import java.util.Iterator;
import java.util.List;

import org.ansj.recognition.Recognition;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 分词结果的一个封装
 * 
 * @author Ansj
 *
 */
public class Result implements Iterable<Term> {

	private List<Term> terms = null;

	public Result(List<Term> terms) {
		this.terms = terms;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	@Override
	public Iterator<Term> iterator() {
		return terms.iterator();
	}

	public int size() {
		return terms.size();
	}

	public Term get(int index) {
		return terms.get(index);
	}

	/**
	 * 调用一个发现引擎
	 * 
	 * @return
	 */
	public Result recognition(Recognition re) {
		re.recognition(this);
		return this;
	}

	@Override
	public String toString() {
		return StringUtil.joiner(this.terms, ",");
	}

}
