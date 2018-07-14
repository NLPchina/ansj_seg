package org.ansj.app.extracting.domain;


import org.ansj.app.extracting.exception.RuleFormatException;

import java.util.*;

/**
 * Created by Ansj on 29/08/2017.
 */
public class Token {
	public static final String ALL = ":*" ;

	private int index;

	private Set<String> terms = new HashSet<String>();

	private List<String> regexs;//正则限定

	private int[] range;//范围限定

	private Token prev ;

	private Token next ;



	public Token(int index, String termsStr) {
		this.index = index;
		String[] split = termsStr.substring(1, termsStr.length() - 1).split("\\|");
		for (String str : split) {
			terms.add(str);
		}
	}

	public void addAttr(String str) throws RuleFormatException {
		char head = str.charAt(0);
		str = str.substring(1, str.length() - 1);
		if (head == '[') {
			if (regexs == null) {
				regexs = new ArrayList<String>();
			}
			regexs.add(str);
		} else if (head == '{') {
			String[] split = str.split(",");
			if (range == null) {
				range = new int[2];
			}
			if (split.length >= 2) {
				range[0] = Integer.parseInt(split[0].trim());
				range[1] = Integer.parseInt(split[1].trim());
			} else {
				range[0] = Integer.parseInt(split[0].trim());
				range[1] = Integer.parseInt(split[0].trim());
			}

		} else {
			throw new RuleFormatException("err format attr by " + str);
		}

	}


	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Set<String> getTerms() {
		return terms;
	}

	public void setTerms(Set<String> terms) {
		this.terms = terms;
	}

	public List<String> getRegexs() {
		if(regexs==null){
			return Collections.emptyList() ;
		}
		return regexs;
	}

	public void setRegexs(List<String> regexs) {
		this.regexs = regexs;
	}

	public int[] getRange() {
		return range;
	}

	public void setRange(int[] range) {
		this.range = range;
	}

	public Token getPrev() {
		return prev;
	}


	public Token getNext() {
		return next;
	}

	public void setNext(Token next) {
		this.next = next;
		next.prev = this;
	}

	@Override
	public String toString() {
		return "index="+index+",terms="+terms+",regexs="+regexs+",range="+ Arrays.toString(range);
	}


}
