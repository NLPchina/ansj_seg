package org.ansj.app.extracting.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by Ansj on 20/09/2017.
 */
public class Rule {
	private List<Token> tokens;
	private Map<String, int[]> groups;
	private Map<String, String> attr;
	private double weight;
	private String ruleStr ;


	public Rule(String ruleStr ,List<Token> tokens, Map<String, int[]> groups, Map<String, String> attr, double weight) {
		this.ruleStr = ruleStr ;
		this.tokens = tokens;
		this.groups = groups;
		this.attr = attr ;
		this.weight = weight;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public void setGroups(Map<String, int[]> groups) {
		this.groups = groups;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public Map<String, int[]> getGroups() {
		return groups;
	}

	public Map<String, String> getAttr() {
		return attr;
	}

	public double getWeight() {
		return weight;
	}

	public String getRuleStr() {
		return ruleStr;
	}
}
