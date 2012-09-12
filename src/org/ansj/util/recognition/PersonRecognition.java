package org.ansj.util.recognition;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.util.TermUtil;

/**
 * 人名识别工具类
 * 
 * @author ansj
 * 
 */
public class PersonRecognition {

	private Term[] terms;

	// public int B = -1;//0 姓氏
	// public int C = -1;//1 双名的首字
	// public int D = -1;//2 双名的末字
	// public int E = -1;//3 单名
	// public int N = -1; //4任意字
	// public int L = -1;//11 人名的下文
	// public int M = -1;//12 两个中国人名之间的成分
	// public int m = -1;//44 可拆分的姓名
	// double[] factory = {"BC", "BCD", "BCDE", "BCDEN"}

	double[] factory = { 0.16403230301872307, 0.7917342275620406, 0.030721874654891644, 0.013511594764344676 };

	public PersonRecognition(Term[] terms) {
		this.terms = terms;
	}

	public void recogntion() {
		Term term = null;
		Term tempTerm = null;
		List<Term> termList = new ArrayList<Term>();
		int beginFreq = 10;
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			if (term == null) {
				continue;
			}
			term.score = 0;
			term.selfScore = 0;
			for (int j = 0; j < 4; j++) {
				if (term.getTermNatures().personAttr.getFreq(j, 0) > 300) {
					tempTerm = nameFind(i, beginFreq, j);
					if (tempTerm != null)
						termList.add(tempTerm);
				} else if (term.getName().length() > 2 && term.getTermNatures().personAttr.getFreq(j, 0) > 0) {
					j = 3;
					tempTerm = nameFind(i, beginFreq, j);
				}
			}

			for (Term term2 : termList) {
				TermUtil.insertTerm(terms, term2);
			}
			beginFreq = term.getTermNatures().personAttr.begin + 1;
		}
	}

	/**
	 * 人名识别
	 * 
	 * @param term
	 * @param offe
	 * @param freq
	 */

	private Term nameFind(int offe, int beginFreq, int size) {
		// TODO Auto-generated method stub

		StringBuilder sb = new StringBuilder();
		PersonNatureAttr pna = null;
		int index = 0;
		int freq = 0;
		double allFreq = 0;
		Term term = null;
		boolean undefinite = false;
		int i = offe;
		for (; i < terms.length; i++) {
			// 走到结尾处识别出来一个名字.
			if (terms[i] == null) {
				continue;
			}
			pna = terms[i].getTermNatures().personAttr;
			// 在这个长度的这个位置的词频,如果没有可能就干掉,跳出循环
			if ((freq = pna.getFreq(size, index)) == 0) {
				if (size == 3 && sb.length() > 4) {
					break;
				} else {
					return null;
				}
			}

			if (freq / (double) (pna.allFreq) < 50) {
				undefinite = true;
			}
			sb.append(terms[i]);
			allFreq += freq;
			index++;

			if (size < 3 && index == size + 2) {
				break;
			}
		}

		double score = Math.log(allFreq) * factory[size];
		double endFreq = 0;
		// 开始寻找结尾词
		boolean flag = true;
		while (flag) {
			i++;
			if (i >= terms.length) {
				endFreq = 10;
				flag = false;
			} else if (terms[i] != null) {
				endFreq = terms[i].getTermNatures().personAttr.end + 1;
				flag = false;
			}
		}

		score += Math.log(endFreq);
		score += Math.log(beginFreq);
		term = new Term(sb.toString(), offe, TermNatures.NR);
		term.selfScore = score;

		if (undefinite && (beginFreq < 3 || endFreq < 3))
			return null;
		// 找到一个词插入进去
		 System.out.print(term + "\t");
		 System.out.print((size + 2) + "\t");
		 System.out.print(Math.log(allFreq) * factory[size] + "\t");
		 System.out.print(beginFreq + "\t");
		 System.out.print(endFreq + "\t");
		 System.out.print(term.selfScore + "\t");
		 System.out.print(undefinite + "\t");
		 System.out.println();
		return term;

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

		// public Term getTerm(double factory) {
		// int begin = this.getBeginFreq();
		// int end = this.getEndFreq();
		// double score = factory * freq + begin + end;
		// // System.out.println(sb + "\t" + score);
		// Term term = new Term(sb.toString(), index, TermNatures.NR);
		// term.selfScore = score;
		// return term;
		// }

	}

	public static void main(String[] args) {
		String str = "希腊第二大党激进左翼联盟党领导人亚历克西斯·齐普拉斯参加示威时说，总理安东尼斯·萨马拉斯正将希腊带向一场“灾难”，“希腊广大民众不久会发起反抗";

		List<Term> all = new ArrayList<Term>();
		for (int i = 0; i < str.length(); i++) {
			all.add(new Term(str.charAt(i) + "", i, TermNatures.NR));
		}
	}

}
