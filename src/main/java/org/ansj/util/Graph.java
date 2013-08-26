package org.ansj.util;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.InitDictionary;
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
		to.clearScore();
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
			from.setTo(to);
			from.clearScore();
			to = from;
		}
		return root;
	}

	 /**
	 * 删除最短的节点,废了好大劲写的.然后发现木有用..伤不起啊.舍不得删.让他进git体验下吧回头我再删掉-_-!
	 */
	 public void rmLittlePath() {
	 int maxTo = -1;
	 Term temp = null;
	 Term maxTerm = null;
	 // 是否有交叉
	 boolean flag = false;
	 int length = terms.length - 1;
	 for (int i = 0; i < length; i++) {
	 maxTerm = getMaxTerm(i);
	
	 if (maxTerm == null)
	 continue;
	
	 maxTo = maxTerm.getToValue();
	
	 /**
	 * 对字数进行优化.如果一个字.就跳过..两个字.且第二个为null则.也跳过.从第二个后开始
	 */
	 switch (maxTerm.getName().length()) {
	 case 1:
	 continue;
	 case 2:
	 if (terms[i + 1] == null) {
	 i = i + 1;
	 continue;
	 }
	 }
	
	 /**
	 * 判断是否有交叉
	 */
	 for (int j = i + 1; j < maxTo; j++) {
	 temp = getMaxTerm(j);
	 if (temp == null) {
	 continue;
	 }
	 if (maxTo < temp.getToValue()) {
	 maxTo = temp.getToValue();
	 flag = true;
	 }
	 }
	
	 if (flag) {
	 i = maxTo - 1;
	 flag = false;
	 } else {
	 maxTerm.setNext(null);
	 terms[i] = maxTerm;
	 for (int j = i + 1; j < maxTo; j++) {
	 terms[j] = null;
	 }
	 }
	 }
	 }

	/**
	 * 得道最到本行最大term
	 * 
	 * @param i
	 * @return
	 */
	private Term getMaxTerm(int i) {
		// TODO Auto-generated method stub
		Term maxTerm = terms[i];
		if (maxTerm == null) {
			return null;
		}
		int maxTo = maxTerm.getToValue();
		Term term = maxTerm;
		while ((term = term.getNext()) != null) {
			if (maxTo < term.getToValue()) {
				maxTo = term.getToValue();
				maxTerm = term;
			}
		}
		return maxTerm;
	}

	/**
	 * 删除无意义的节点,防止viterbi太多
	 */
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
			char c = str.charAt(to);
			TermNatures tn = InitDictionary.termNatures[c];
			if (tn == null)
				tn = TermNatures.NULL;
			terms[to] = new Term(String.valueOf(c), to, tn);
			terms[to].setPathScore(fromTerm);
		}
	}

	/**
	 * 根据分数
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
				term.setPathSelfScore(fromTerm);
				term = term.getNext();
			}
		}

	}

}
