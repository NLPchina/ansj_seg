package org.ansj.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.NatureLibrary;
import org.ansj.library.company.CompanyAttrLibrary;
import org.ansj.recognition.ForeignPersonRecognition;

/**
 * term的操作类
 * 
 * @author ansj
 * 
 */
public class TermUtil {

	/**
	 * 将两个term合并为一个全新的term
	 * 
	 * @param termNatures
	 * @return
	 */
	public static Term makeNewTermNum(Term from, Term to, TermNatures termNatures) {
		Term term = new Term(from.getName() + to.getName(), from.getOffe(), termNatures);
		term.termNatures().numAttr = from.termNatures().numAttr;
		TermUtil.termLink(term, to.to());
		TermUtil.termLink(term.from(), term);
		return term;
	}

	public static void termLink(Term from, Term to) {
		if (from == null || to == null)
			return;
		from.setTo(to);
		to.setFrom(from);
	}

	/**
	 * 将一个term插入到链表中的对应位置中, 如果这个term已经存在参照type type 0.跳过 1. 替换 2.累积分值 保证顺序,由大到小
	 * 
	 * @param terms
	 * @param term
	 */
	public static void insertTerm(Term[] terms, Term term, int type) {
		Term self = terms[term.getOffe()];

		if (self == null) {
			terms[term.getOffe()] = term;
			return;
		}

		int len = term.getName().length();

		// 如果是第一位置
		if (self.getName().length() == len) {
			if (type == 1) {
				term.setNext(self.next());
				terms[term.getOffe()] = term;
			} else if (type == 2) {
				self.score(self.score() + term.score());
				self.selfScore(self.selfScore() + term.selfScore());
			}
			return;
		}
		
		if(self.getName().length() > len){
			term.setNext(self) ;
			terms[term.getOffe()] = term;
			return;
		}

		Term next = self;
		Term before = self;
		while ((next = before.next()) != null) {
			if (next.getName().length() == len) {
				if (type == 1) {
					term.setNext(next.next());
					before.setNext(term);
				} else if (type == 2) {
					next.score(next.score() + term.score());
					next.selfScore(next.selfScore() + term.selfScore());
				}
				return;
			} else if (next.getName().length() > len) {
				before.setNext(term);
				term.setNext(next);
				return;
			}
			before = next;
		}

		before.setNext(term); // 如果都没有命中
	}

	public static void insertTermNum(Term[] terms, Term term) {
		terms[term.getOffe()] = term;
	}

	public static void insertTerm(Term[] terms, List<Term> tempList, TermNatures nr) {
		StringBuilder sb = new StringBuilder();
		int offe = tempList.get(0).getOffe();
		for (Term term : tempList) {
			sb.append(term.getName());
			terms[term.getOffe()] = null;
		}
		Term term = new Term(sb.toString(), offe, TermNatures.NR);
		insertTermNum(terms, term);
	}

	protected static Term setToAndfrom(Term to, Term from) {
		// TODO Auto-generated method stub
		from.setTo(to);
		to.setFrom(from);
		return from;
	}

	private static final HashMap<String, int[]> companyMap = CompanyAttrLibrary.getCompanyMap();

	/**
	 * 得到细颗粒度的分词，并且确定词性
	 * 
	 * @return 返回是null说明已经是最细颗粒度
	 */
	public static void parseNature(Term term) {
		if (!Nature.NW.equals(term.natrue())) {
			return;
		}

		String name = term.getName();

		if (name.length() <= 3) {
			return;
		}

		// 是否是外国人名
		if (ForeignPersonRecognition.isFName(name)) {
			term.setNature(NatureLibrary.getNature("nrf"));
			return;
		}

		List<Term> subTerm = term.getSubTerm();

		// 判断是否是机构名
		term.setSubTerm(subTerm);
		Term first = subTerm.get(0);
		Term last = subTerm.get(subTerm.size() - 1);
		int[] is = companyMap.get(first.getName());
		int all = 0;

		is = companyMap.get(last.getName());
		if (is != null) {
			all += is[1];
		}

		if (all > 1000) {
			term.setNature(NatureLibrary.getNature("nt"));
			return;
		}
	}

	/**
	 * 从from到to生成subterm
	 * 
	 * @param terms
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<Term> getSubTerm(Term from, Term to) {
		// TODO Auto-generated method stub
		List<Term> subTerm = new ArrayList<Term>(3);

		while ((from = from.to()) != to) {
			subTerm.add(from);
		}

		return subTerm;
	}

}
