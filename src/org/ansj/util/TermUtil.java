package org.ansj.util;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;

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
		}else{
			if(temp.getNext()!=null){
				term.setNext(temp.getNext()) ;
			}
			temp.setNext(term) ;
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
}
