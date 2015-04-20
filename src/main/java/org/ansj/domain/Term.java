package org.ansj.domain;

import java.util.List;

import org.ansj.util.MathUtil;

public class Term implements Comparable<Term> {
	// 当前词
	private String name;
	//
	private String realName;
	// 当前词的起始位置
	private int offe;
	// 词性列表
	private TermNatures termNatures = TermNatures.NULL;
	// 词性列表
	private AnsjItem item = AnsjItem.NULL;
	// 同一行内数据
	private Term next;
	// 分数
	private double score = 0;
	// 本身分数
	private double selfScore = 1;
	// 起始位置
	private Term from;
	// 到达位置
	private Term to;
	// 本身这个term的词性.需要在词性识别之后才会有值,默认是空
	private Nature nature = Nature.NULL;

	private List<Term> subTerm = null;

	public Term(String name, int offe, AnsjItem item) {
		super();
		this.name = name;
		this.offe = offe;
		this.item = item;
		if (item.termNatures != null) {
			this.termNatures = item.termNatures;
			if (termNatures.nature != null) {
				this.nature = termNatures.nature;
			}
		}
	}

	public Term(String name, int offe, TermNatures termNatures) {
		super();
		this.name = name;
		this.offe = offe;
		this.termNatures = termNatures;
		if (termNatures.nature != null) {
			this.nature = termNatures.nature;
		}
	}

	public Term(String name, int offe, String natureStr, int natureFreq) {
		super();
		this.name = name;
		this.offe = offe;
		TermNature termNature = new TermNature(natureStr, natureFreq);
		this.nature = termNature.nature;
		this.termNatures = new TermNatures(termNature);
	}

	// 可以到达的位置
	public int toValue() {
		return offe + name.length();
	}

	public int getOffe() {
		return offe;
	}

	public void setOffe(int offe) {
		this.offe = offe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 核心构建最优的路径
	 * 
	 * @param term
	 */
	public void setPathScore(Term from) {
		// 维特比进行最优路径的构建
		double score = MathUtil.compuScore(from, this);
		if (this.from == null || this.score >= score) {
			this.setFromAndScore(from, score);
		}
	}

	/**
	 * 核心分数的最优的路径,越小越好
	 * 
	 * @param term
	 */
	public void setPathSelfScore(Term from) {
		double score = this.selfScore + from.score;
		// 维特比进行最优路径的构建
		if (this.from == null || this.score > score) {
			this.setFromAndScore(from, score);
		}
	}

	private void setFromAndScore(Term from, double score) {
		// TODO Auto-generated method stub
		this.from = from;
		this.score = score;
	}

	/**
	 * 进行term合并
	 * 
	 * @param term
	 * @param maxNature
	 */
	public Term merage(Term to) {
		this.name = this.name + to.getName();
		return this;
	}

	/**
	 * 更新偏移量
	 * 
	 * @param offe
	 */
	public void updateOffe(int offe) {
		this.offe += offe;
	}

	public Term getNext() {
		return next;
	}

	/**
	 * 返回他自己
	 * 
	 * @param next
	 *            设置他的下一个
	 * @return
	 */
	public Term setNext(Term next) {
		this.next = next;
		return this;
	}

	public Term from() {
		return from;
	}

	public Term to() {
		return to;
	}

	public void setFrom(Term from) {
		this.from = from;
	}

	public void setTo(Term to) {
		this.to = to;
	}

	/**
	 * 获得这个term的所有词性
	 * 
	 * @return
	 */
	public TermNatures termNatures() {
		return termNatures;
	}

	@Override
	public int compareTo(Term o) {
		// TODO Auto-generated method stub
		if (this.score > o.score) {
			return 0;
		} else {
			return 1;
		}
	}

	public void setNature(Nature nature) {
		// TODO Auto-generated method stub
		this.nature = nature;
	}

	/**
	 * 获得这个词的词性.词性计算后才可生效
	 * 
	 * @return
	 */
	public Nature natrue() {
		return nature;
	}

	public String getNatureStr() {
		return nature.natureStr;
	}

	@Override
	public String toString() {
		if ("null".equals(nature.natureStr)) {
			return name;
		}
		return this.getRealName() + "/" + nature.natureStr;
	}

	/**
	 * 将term的所有分数置为0
	 */
	public void clearScore() {
		this.score = 0;
		this.selfScore = 0;
	}

	public void setSubTerm(List<Term> subTerm) {
		this.subTerm = subTerm;
	}

	public List<Term> getSubTerm() {
		return subTerm;
	}

	public String getRealName() {
		if (realName == null) {
			return name;
		}
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public double score() {
		return this.score;
	}

	public void score(double score) {
		this.score = score;
	}

	public double selfScore() {
		return this.selfScore;
	}

	public void selfScore(double selfScore) {
		this.selfScore = selfScore;
	}

	public AnsjItem item() {
		return this.item;
	}

}
