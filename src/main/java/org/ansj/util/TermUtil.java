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
		term.getTermNatures().numAttr = from.getTermNatures().numAttr;
		TermUtil.termLink(term, to.getTo());
		TermUtil.termLink(term.getFrom(), term);
		return term;
	}

	public static void termLink(Term from, Term to) {
		if (from == null || to == null)
			return;
		from.setTo(to);
		to.setFrom(from);
	}

	/**
	 * 将一个term插入到链表中的对应位置中,不排序了.
	 * 
	 * @param terms
	 * @param term
	 */
	public static void insertTerm(Term[] terms, Term term) {
		Term temp = terms[term.getOffe()];
		if (temp == null) {
			terms[term.getOffe()] = term;
		} else {
			if (temp.getNext() != null) {
				term.setNext(temp.getNext());
			}
			temp.setNext(term);
		}
	}

	public static void insertTermNum(Term[] terms, Term term) {
		// TODO Auto-generated method stub
		terms[term.getOffe()] = term;

	}

	public static void insertTerm(Term[] terms, List<Term> tempList, TermNatures nr) {
		// TODO Auto-generated method stub
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
		if (!Nature.NW.equals(term.getNatrue())) {
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

		while ((from = from.getTo()) != to) {
			subTerm.add(from);
		}

		return subTerm;
	}

}
