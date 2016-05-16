package org.ansj.recognition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 基于规则的新词发现 jijiang feidiao
 * 
 * @author ansj
 * 
 */
public class RuleRecognition {

	private static final Nature nature = new Nature("book");

	private static Map<String, String> ruleMap = new HashMap<String, String>();

	static {
		ruleMap.put("《", "》");
	}

	public static List<Term> recognition(List<Term> terms) {
		String end = null;
		String name;

		LinkedList<Term> mergeList = null;

		List<Term> result = new LinkedList<Term>();

		for (Term term : terms) {
			name = term.getName();
			if (end == null) {
				if ((end = ruleMap.get(name)) != null) {
					mergeList = new LinkedList<Term>();
					mergeList.add(term);
				} else {
					result.add(term);
				}
			} else {
				mergeList.add(term);
				if (end.equals(name)) {

					Term ft = mergeList.pollFirst();
					for (Term sub : mergeList) {
						ft.merage(sub);
					}
					ft.setNature(nature);
					result.add(ft);
					mergeList = null;
					end = null;
				}
			}
		}

		if (mergeList != null) {
			for (Term term : result) {
				result.add(term);
			}
		}

		return result;
	}

	public static void main(String[] args) {
		List<Term> parse = ToAnalysis.parse("我喜欢看《毛泽东思想》全集！");

		parse = RuleRecognition.recognition(parse);

		System.out.println(parse);
	}

}
