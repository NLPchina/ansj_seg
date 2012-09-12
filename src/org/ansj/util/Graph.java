package org.ansj.util;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.util.recognition.NumRecognition;
import org.ansj.util.recognition.PersonRecognition;
import org.ansj.util.recognition.UserDefineRecognition;

/**
 * 最短路径
 * 
 * @author ansj
 * 
 */
public class Graph {
	private String str = null;
	private Term[] terms = null;
	private Term end = null;
	private Term root = null;
	private static final String E = "末##末";
	private static final String B = "始##始";

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
	public Merger getPath() {
		return new Merger();
	}

	public void addTerm(Term term) {
		// 将词放到图的位置
		if (terms[term.getOffe()] == null) {
			terms[term.getOffe()] = term;
		} else {
			terms[term.getOffe()] = term.setNext(terms[term.getOffe()]);
		}
	}

	private StringBuilder sb = new StringBuilder();

	public String toString() {
		showPath(end.getTo());
		return sb.toString();
	}

	/**
	 * 显示路径
	 * 
	 * @param term
	 */
	private void showPath(Term term) {
		if (term.getFrom() != null) {
			showPath(term.getFrom());
		}
		if (term.getOffe() < terms.length - 1) {
			sb.append(term.toString());
		}
	}

	public void print() {
		Term term = null;
		for (int i = 0; i < terms.length; i++) {
			term = terms[i];
			while (term != null) {
				System.out.print(term + ":" + term.getScore() + "\t");
				term = term.getNext();
			}
			System.out.println();
		}
	}

	/**
	 * 取得最优路径的root Term
	 * 
	 * @return
	 */
	private Term optimalRoot() {
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

	private void rmLittlePath() {
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
					i = temp.getToValue();
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

	private Term setToAndfrom(Term to, Term from) {
		// TODO Auto-generated method stub
		from.setTo(to);
		to.setFrom(from);
		return from;
	}

	/**
	 * 合并term.
	 * 
	 * @author ansj
	 * 
	 */
	public class Merger {
		/**
		 * 寻找最优路径
		 * 
		 * @param yuan
		 * @return
		 */
		public Merger merger() {

			rmLittlePath();
			// 最短路径
			walkPath();

			// 数字发现
			NumRecognition.recogntionNM(terms);
			rmLittlePath();

			// 数字+量词的识别
			NumRecognition.recogntionNQ(terms);

			// 姓名识别
			new PersonRecognition(terms).recogntion();
			walkPathByScore();
						
			// 用户自定义词典的识别
			new UserDefineRecognition(terms).recongnitionTerm();
			rmLittlePath();
			walkPathByFreq();

			return this;

		}

		private void walkPathByScore() {
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

		private void walkPathByFreq() {
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

		private void walkPath() {
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

		/**
		 * 将最终结果放到Term数组中
		 */
		public List<Term> getResult() {
			List<Term> result = new ArrayList<Term>();
			int length = terms.length - 1;
			for (int i = 0; i < length; i++) {
				if (terms[i] != null) {
					result.add(terms[i]);
				}
			}
			return result;
		}
	}
}
