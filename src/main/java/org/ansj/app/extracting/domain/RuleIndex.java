package org.ansj.app.extracting.domain;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ansj on 21/09/2017.
 */
public class RuleIndex {

	private HashMap<String, Set<Rule>> ruleIndex = new HashMap<String, Set<Rule>>();

	private Forest forest = new Forest();

	/**
	 * 增加规则到RuleIndex
	 * @param rule
	 */
	public void add(Rule rule) {
		List<Token> tokens = rule.getTokens();
		for (Token token : tokens) {
			for (String str : token.getTerms()) {
				if (str.charAt(0) != ':') {
					Library.insertWord(forest, new Value(str, "rule", "10000"));
				}
			}
		}


		for (String str : tokens.get(0).getTerms()) {
			Set<Rule> rules = ruleIndex.get(str);
			if(rules==null){
				rules = new HashSet<>() ;
				ruleIndex.put(str,rules) ;
			}
			rules.add(rule) ;
		}
	}

	public Forest getForest() {
		return forest;
	}

	public Set<Rule> getRules(String key){
		return ruleIndex.get(key) ;
	}
}
