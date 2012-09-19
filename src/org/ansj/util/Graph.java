package org.ansj.util;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.splitWord.Analysis.Merger;

/**
 * 最短路径
 * 
 * @author ansj
 * 
 */
public class Graph {
	protected String str = null;
	public Term[] terms = null;
	protected Term end = null;
	protected Term root = null;
	protected static final String E = "末##末";
	protected static final String B = "始##始";
	// 是否有人名
	public boolean hasPerson;
	// 是否有数字
	public boolean hasNum;
	// 是否需有歧异

	public Graph(String str) {
		this.str = str;
		int size = str.length();
		terms = new Term[size + 1];
		end = new Term(E, size, TermNatures.END);
		root = new Term(B, -1, TermNatures.BEGIN);
		terms[size] = end;
	}

	/**
	 * 构建最优路径
	 */
	public List<Term> getResult(Merger merger) {
		return merger.merger();
	}

	/**
	 * 增加一个词语到图中
	 * 
	 * @param term
	 */
	public void addTerm(Term term) {
		// 是否有数字
		if (!hasNum && term.getTermNatures().numAttr.numFreq > 0) {
			hasNum = true;
		}
		// 是否有人名
		if (!hasPerson && term.getTermNatures().personAttr.flag) {
			hasPerson = true;
		}
		// 将词放到图的位置
		if (terms[term.getOffe()] == null) {
			terms[term.getOffe()] = term;
		} else {
			terms[term.getOffe()] = term.setNext(terms[term.getOffe()]);
		}
	}

	/**
	 * 取得最优路径的root Term
	 * 
	 * @return
	 */
	protected Term optimalRoot() {
		Term to = end;
		Term from = null;
		while ((from = to.getFrom()) != null) {
			for (int i = from.getOffe() + 1; i < to.getOffe(); i++) {
				terms[i] = null;
			}
			if (from.getOffe() > -1) {
				terms[from.getOffe()] = from;
			}
			// 断开横向链表.节省内存
			from.setNext(null);
			to = setToAndfrom(to, from);
		}
		return root;
	}

	public void rmLittlePath() {
		int maxTo = -1;
		Term temp = null;
		boolean flag = false;
		for (int i = 0; i < terms.length; i++) {
			if (terms[i] == null)
				continue;
			maxTo = terms[i].getToValue();
			if (maxTo - i == 1 || i + 1 == terms.length)
				continue;
			flag = false;
			for (int j = i + 1; j < maxTo; j++) {
				temp = terms[j];
				if (temp != null && temp.getToValue() > maxTo) {
					flag = true;
					i = temp.getToValue() - 1;
					break;
				}
			}

			if (!flag) {
				terms[i].setNext(null);
				for (int j = i + 1; j < maxTo; j++) {
					terms[j] = null;
				}
				i = maxTo - 1;
			}
		}
	}

	public void rmLittleSinglePath() {
		int maxTo = -1;
		Term temp = null;
		for (int i = 0; i < terms.length; i++) {
			if (terms[i] == null)
				continue;
			maxTo = terms[i].getToValue();
			if (maxTo - i == 1 || i + 1 == terms.length)
				continue;
			for (int j = i; j < maxTo; j++) {
				temp = terms[j];
				if (temp != null && temp.getToValue() <= maxTo && temp.getName().length() == 1) {
					terms[j] = null;
				}
			}
		}
	}

	protected Term setToAndfrom(Term to, Term from) {
		// TODO Auto-generated method stub
		from.setTo(to);
		to.setFrom(from);
		return from;
	}

	public void walkPathByScore() {
		// TODO Auto-generated method stub
		Term term = null;
		// BEGIN先行打分
		mergerByScore(root, 0);
		// 从第一个词开始往后打分
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			while (term != null && term.getFrom() != null && term != end) {
				int to = term.getToValue();
				mergerByScore(term, to);
				term = term.getNext();
			}
		}
		optimalRoot();
	}

	public void walkPathByFreq() {
		// TODO Auto-generated method stub
		Term term = null;
		// BEGIN先行打分
		mergerFreq(root, 0);
		// 从第一个词开始往后打分
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			while (term != null && term.getFrom() != null && term != end) {
				int to = term.getToValue();
				mergerFreq(term, to);
				term = term.getNext();
			}
		}
		optimalRoot();
	}
	public void walkPath() {
		Term term = null;
		// BEGIN先行打分
		merger(root, 0);
		// 从第一个词开始往后打分
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			while (term != null && term.getFrom() != null && term != end) {
				int to = term.getToValue();
				merger(term, to);
				term = term.getNext();
			}
		}
		optimalRoot();
	}

	/**
	 * 具体的遍历打分方法
	 * 
	 * @param i
	 *            起始位置
	 * @param j
	 *            起始属性
	 * @param to
	 */
	private void merger(Term fromTerm, int to) {
		Term term = null;
		if (terms[to] != null) {
			term = terms[to];
			while (term != null) {
				// 关系式to.set(from)
				term.setPathScore(fromTerm);
				term = term.getNext();
			}
		} else {
			terms[to] = new Term(String.valueOf(str.charAt(to)), to, TermNatures.NULL);
			terms[to].setPathScore(fromTerm);
		}
	}

	/**
	 * 根据词长打分方法
	 * 
	 * @param i
	 *            起始位置
	 * @param j
	 *            起始属性
	 * @param to
	 */
	private void mergerFreq(Term fromTerm, int to) {
		Term term = null;
		if (terms[to] != null) {
			term = terms[to];
			if (term != null) {
				// 关系式to.set(from)
				term.setPathScoreByFreq(fromTerm);
			}
		}
	}

	/**
	 * 人名打分方法
	 * 
	 * @param i
	 *            起始位置
	 * @param j
	 *            起始属性
	 * @param to
	 */
	private void mergerByScore(Term fromTerm, int to) {
		Term term = null;
		if (terms[to] != null) {
			term = terms[to];
			while (term != null) {
				// 关系式to.set(from)
				term.setPathPersonScore(fromTerm);
				term = term.getNext();
			}
		}

	}

}
