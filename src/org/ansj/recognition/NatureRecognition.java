package org.ansj.recognition;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.util.MathUtil;

/**
 * 词性标注工具类
 * 
 * @author ansj
 * 
 */
public class NatureRecognition {

	private NatureTerm root = new NatureTerm(TermNature.BEGIN);

	private NatureTerm[] end = { new NatureTerm(TermNature.END) };

	private List<Term> terms = null;

	private NatureTerm[][] natureTermTable = null;

	/**
	 * 构造方法.传入分词的最终结果
	 * 
	 * @param terms
	 */
	public NatureRecognition(List<Term> terms) {
		this.terms = terms;
		natureTermTable = new NatureTerm[terms.size() + 1][];
		natureTermTable[terms.size()] = end;
	}

	/**
	 * 进行最佳词性查找,引用赋值.所以不需要有返回值
	 */
	public void recognition() {
		int length = terms.size();
		for (int i = 0; i < length; i++) {
			natureTermTable[i] = getNatureTermArr(terms.get(i).getTermNatures().termNatures);
		}
		walk();
	}

	public void walk() {
		int length = natureTermTable.length - 1;
		setScore(root, natureTermTable[0]);
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < natureTermTable[i].length; j++) {
				setScore(natureTermTable[i][j], natureTermTable[i + 1]);
			}
		}
		optimalRoot();
	}

	private void setScore(NatureTerm natureTerm, NatureTerm[] natureTerms) {
		// TODO Auto-generated method stub
		for (int i = 0; i < natureTerms.length; i++) {
			natureTerms[i].setScore(natureTerm);
		}
	}


	private NatureTerm[] getNatureTermArr(TermNature[] termNatures) {
		NatureTerm[] natureTerms = new NatureTerm[termNatures.length];
		for (int i = 0; i < natureTerms.length; i++) {
			natureTerms[i] = new NatureTerm(termNatures[i]);
		}
		return natureTerms;
	}

	/**
	 * 获得最优路径
	 */
	private void optimalRoot() {
		NatureTerm to = end[0];
		NatureTerm from = null;
		int index = natureTermTable.length - 1;
		while ((from = to.from) != null && index > 0) {
			terms.get(--index).setNature(from.termNature.nature);
			to = from ;
		}
	}

	/**
	 * 关于这个term的词性
	 * 
	 * @author ansj
	 * 
	 */
	public class NatureTerm {

		public TermNature termNature;

		public double score = 0;

		public double selfScore;

		public NatureTerm from;

		protected NatureTerm(TermNature termNature) {
			this.termNature = termNature;
			selfScore = termNature.frequency + 1;
		}

		public void setScore(NatureTerm natureTerm) {
			// TODO Auto-generated method stub
			double tempScore = MathUtil.compuNatureFreq(natureTerm, this);
			if (from == null || score < tempScore) {
				this.score = tempScore;
				this.from = natureTerm;
			}
		}

		@Override
		public String toString() {
			return termNature.nature.natureStr + "/" + selfScore;
		}

	}
}
