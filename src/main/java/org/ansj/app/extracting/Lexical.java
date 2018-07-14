package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.Rule;
import org.ansj.app.extracting.domain.Token;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.nlpcn.commons.lang.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ansj on 29/08/2017.
 */
public class Lexical {

	private static final Map<Character, Character> MAP = new HashMap<Character, Character>();

	static {
		MAP.put('(', ')');
		MAP.put('[', ']');
		MAP.put('{', '}');
	}


	public static Rule parse(String line) throws RuleFormatException {

		if (StringUtil.isBlank(line)) {
			return null;
		}

		List<Token> tokens = new ArrayList<Token>();

		Map<String, int[]> groups = new HashMap<String, int[]>();
		Map<String, String> attr = new HashMap<String, String>();

		double weight = 1d;

		String[] split = line.split("\t");

		String rule = split[0];

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < rule.length(); i++) {
			switch (rule.charAt(i)) {
				case ' ':
					break;
				case '(':
				case '{':
				case '[':
					i = makeToken(list, rule, i);
					break;
				default:
					throw new RuleFormatException(rule + " not begin ( at index " + i);

			}

		}

		Token token = null;

		int index = 0;

		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if (str.startsWith("(")) {
				Token tempToken = new Token(index, str);
				index++ ;
				tokens.add(tempToken);
				if (token != null) {
					token.setNext(tempToken);
				}
				token = tempToken;
			} else {
				token.addAttr(str);
			}
		}

		if (split.length > 1 && StringUtil.isNotBlank(split[1])) {
			String[] split1 = split[1].split(";");
			for (int i = 0; i < split1.length; i++) {
				String[] split2 = split1[i].split(":");
				if (split2[1].startsWith("(")) {
					attr.put(split2[0], split2[1].substring(1, split2[1].length() - 1));
				} else {
					String[] split3 = split2[1].split(",");
					int[] ints = new int[split3.length];
					for (int j = 0; j < ints.length; j++) {
						ints[j] = Integer.parseInt(split3[j].trim());
					}
					groups.put(split2[0], ints);
				}

			}
		}

		if (split.length > 2 && StringUtil.isNotBlank(split[2])) {
			weight = Double.parseDouble(split[2]);
		}

		return new Rule(line, tokens, groups, attr, weight);
	}

	private static int makeToken(List<String> list, String rule, int i) {
		int num = 0;
		StringBuilder sb = new StringBuilder();
		char c;
		char begin = rule.charAt(i);
		char end = MAP.get(begin);
		int j = i;
		for (; j < rule.length(); j++) {
			c = rule.charAt(j);
			if (c == '\\') {
				j++;
				sb.append(rule.charAt(j));
				continue;
			}
			sb.append(c);
			if (c == begin) {
				num++;
			} else if (c == end) {
				num--;
			}

			if (num == 0) {
				break;
			}

		}
		list.add(sb.toString());
		return j;
	}


}