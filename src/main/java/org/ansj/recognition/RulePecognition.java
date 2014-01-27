package org.ansj.recognition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Nature;
import org.ansj.domain.NewWord;
import org.ansj.util.Graph;

/**
 * 基于规则的新词发现
 * 
 * @author ansj
 * 
 */
public class RulePecognition {

	private static Map<String, String> ruleMap = new HashMap<String, String>();

	static {
		ruleMap.put("《", "》");
	}

	public static List<NewWord> recognition(Graph graph) {
		return recognition(graph.str);
	}

	public static List<NewWord> recognition(String str) {
		String end = null;
		StringBuilder sb = null;
		String name;
		List<NewWord> result = new ArrayList<NewWord>();
		for (int i = 0; i < str.length(); i++) {
			name = String.valueOf(str.charAt(i));
			if (end == null) {
				if ((end = ruleMap.get(name)) != null) {
					sb = new StringBuilder();
				}
			} else {
				if (end.equals(name)) {
					result.add(new NewWord(sb.toString(), Nature.NW, -sb.length()));
					sb = null;
					end = null;
				} else {
					sb.append(name);
				}
			}
		}
		return result;
	}
}
