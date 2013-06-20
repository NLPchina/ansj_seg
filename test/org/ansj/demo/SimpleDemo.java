package org.ansj.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

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
		List<Term> paser = BaseAnalysis.parse(str);
		// 词性标注
		new NatureRecognition(paser).recognition();

		//合并用户id
		paser = mergerId(paser) ;
		System.out.println(paser);
	}

	private static final Nature userIdNature = new Nature("userId");

	public static List<Term> mergerId(List<Term> paser) {
		List<Term> result = new ArrayList<Term>();
		Term term = null ;
		Term newTerm = null ;
		for (int i = 0; i < paser.size(); i++) {
			term = paser.get(i) ;
			if ("@".equals(term.getName())) {
				StringBuilder sb = new StringBuilder(term.getName());
				int end = mergerId(paser, sb, i);
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
				result.add(paser.get(i)) ;
			}
		}
		return result ;
	}

	private static int mergerId(List<Term> paser, StringBuilder sb, int i) {
		// TODO Auto-generated method stub
		Term term = null;
		String natureStr = null;
		int j = i + 1;
		for (; j < paser.size(); j++) {
			term = paser.get(j);
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
