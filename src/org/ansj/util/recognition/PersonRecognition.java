package org.ansj.util.recognition;

import java.util.List;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.util.TermUtil;
import org.ansj.util.tire.Branch;
import org.ansj.util.tire.TireTree;

/**
 * 人名识别工具类
 * 
 * @author ansj
 * 
 */
public class PersonRecognition {

	// public int B = -1;//1 姓氏
	// public int C = -1;//2 双名的首字
	// public int D = -1;//3 双名的末字
	// public int E = -1;//4 单名
	// public int F = -1;//5 前缀
	// public int G = -1;//6 后缀
	// // *Tag = K( 10), Count = 0, 人名的上文
	// public int L = -1;//11 人名的下文
	// public int M = -1;//12 两个中国人名之间的成分
	// public int N = -1;//13 <无>
	// // *Tag = U( 20), Count = 0, 人名的上文与姓氏成词
	// // *Tag = V( 21), Count = 0, 人名的末字与下文成词
	// public int X = -1;//23 姓与双名首字成词
	// public int Y = -1;//24 姓与单名成词
	// public int Z = -1;//25 双名本身成词
	// public int m = -1;//44 可拆分的姓名
	// 1,2,3 BCD
	// 1,1,2,3 BBCD
	// 1,1,3 BBC

	// String[] parretn = {"BBCD", "BBC", "BBE", "BBZ", "BCD",
	// "BEE", "BE", "BG", "BXD", "BZ", "CDCD", "CD",
	// "EE", "FB", "XD"}

	private static TireTree tire;

	static {
		int[][] sPatterns = {  { 1, 1, 3 }, { 1, 1, 4 }, { 1, 1, 25 }, { 1, 2, 3 }, { 1, 4, 4 }, { 1, 4 }, { 1, 6 }, { 1, 23, 3 }, { 1, 25 },
				{ 1, 3 } };
		double dFactor[] = { 0.000021, 0.001314, 0.000315, 0.656624, 0.000021, 0.146116, 0.009136, 0.000042, 0.038971, 0.000001, 0.000001 };
		tire = new TireTree(sPatterns, dFactor);

	}

	private Term[] terms;

	public void recognition(Term[] terms) {
		this.terms = terms;
		Term term = null;
		Branch branch = tire.getRoot();
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			if (term != null) {
				term.score = 0;
				term.selfScore = 0;
				if (term.getTermNatures().personAttr.flag) {
					termNext(term, branch, new Person(term));
				}
			}
		}
	}

	public void termNext(Term term, Branch branch, Person name) {
		if (term != null && term.getTermNatures().personAttr.hm != null) {
			Branch temp = null;
			List<Entry<Integer, Integer>> entries = null;
			entries = term.getTermNatures().personAttr.getAttrList();
			Person tempName = null;

			for (Entry<Integer, Integer> entry : entries) {
				temp = branch.get(entry.getKey());
				if (temp != null) {
					if (temp.value > -1) {
						Person value = new Person(name, term, entry.getValue());
						Term result = value.getTerm(temp.value);
						// System.out.println(result+"\t"+result.selfScore);
						if (result != null)
							TermUtil.insertTerm(terms, result);
					}
					tempName = new Person(name, term, entry.getValue());
					termNext(term.getTo(), temp, tempName);
				}
			}
		}
	}

	static class Person {
		private int index;
		private Term end;
		private Term begin;
		private StringBuilder sb = new StringBuilder();
		private int freq;

		public Person(Term term) {
			this.index = term.getOffe();
			this.begin = term.getFrom();
		}

		public Person(Person name, Term term, int freq) {
			this.index = name.index;
			this.begin = name.begin;
			this.sb.append(name.sb);
			this.sb.append(term.getName());
			this.end = term.getTo();
			this.freq += freq;
		}

		public String toString() {
			return this.sb.toString();
		}

		public Term getTerm(double factory) {
			int begin = this.getBeginFreq();
			int end = this.getEndFreq();
			double score = factory * freq + begin + end;
			// System.out.println(sb + "\t" + score);
			Term term = new Term(sb.toString(), index, TermNatures.NR);
			term.selfScore = score;
			return term;
		}

		private int getBeginFreq() {
			if (begin.getTermNatures().personAttr.hm == null) {
				return 1;
			}
			Integer temp = begin.getTermNatures().personAttr.hm.get(12);
			if (temp != null && temp > 0) {
				return temp;
			} else {
				return 1;
			}
		}

		private int getEndFreq() {
			if (end.getTermNatures().personAttr.hm == null) {
				return 1;
			}
			Integer temp1 = end.getTermNatures().personAttr.hm.get(12);
			Integer temp2 = end.getTermNatures().personAttr.hm.get(11);
			if (temp1 == null)
				temp1 = 1;
			if (temp2 == null)
				temp2 = 1;
			return Math.max(temp1, temp2);

		}
	}

	public static void main(String[] args) {
		System.out.println(Math.log(0.0001));
	}

}
