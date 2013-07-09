package org.ansj.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;

/**
 * 最最最简单的分词调用方式
 * 
 * @author ansj
 * 
 */
public class SimpleDemo {
	public static void main(String[] args) throws IOException {
		String str = "java@ID:6321-000301@你好";
		// 普通分词
		List<Term> parse = BaseAnalysis.parse(str);
		// 词性标注
		new NatureRecognition(parse).recognition();

		//合并用户id
		parse = mergerId(parse) ;
		System.out.println(parse);
	}

	private static final Nature userIdNature = new Nature("userId");

	public static List<Term> mergerId(List<Term> parse) {
		List<Term> result = new ArrayList<Term>();
		Term term = null ;
		Term newTerm = null ;
		for (int i = 0; i < parse.size(); i++) {
			term = parse.get(i) ;
			if ("@".equals(term.getName())) {
				StringBuilder sb = new StringBuilder(term.getName());
				int end = mergerId(parse, sb, i);
				System.out.println(end);
				if (end > 0) {
					newTerm = new Term(sb.toString(), term.getOffe(), null);
					newTerm.setNature(userIdNature) ;
					result.add(newTerm);
					i = end;
				}else{
					result.add(term) ;
				}
			}else{
				result.add(parse.get(i)) ;
			}
		}
		return result ;
	}

	private static int mergerId(List<Term> parse, StringBuilder sb, int i) {
		// TODO Auto-generated method stub
		Term term = null;
		String natureStr = null;
		int j = i + 1;
		for (; j < parse.size(); j++) {
			term = parse.get(j);
			natureStr = term.getNatrue().natureStr;
			if ("en".equals(natureStr) || "m".equals(natureStr) || "-".equals(term.getName())||":".equals(term.getName())) {
				sb.append(term.getName());
			} else if ("@".equals(term.getName())) {
				sb.append(term.getName());
				break ;
			} else {
				return -1;
			}
		}

		if (sb.length() > 2) {
			return j;
		} else {
			return -1;
		}
	}
}
