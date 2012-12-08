package org.ansj.domain;

import org.ansj.util.MathUtil;

/**
 * 新词发现,实体名
 * 
 * @author ansj
 * 
 */
public class NewWord {
	// 名字
	private String name;
	// 分数
	private double score;
	private double selfScore;
	// 出现次数
	private int freq;
	// 词性
	private TermNatures nature;

	public NewWord(String name, int freq, TermNatures nature) {
		this.name = name;
		this.freq = freq;
		this.nature = nature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public TermNatures getNature() {
		return nature;
	}

	public void setNature(TermNatures nature) {
		this.nature = nature;
	}

	/**
	 * 更新发现权重
	 * 
	 * @param version
	 * @param i
	 * @param nw
	 */
	public void update(int i, TermNatures nw) {
		// TODO Auto-generated method stub
		this.freq += i;
		if (nw == null || !TermNatures.NW.equals(nw)) {
			this.nature = nw;
		}
		this.score += selfScore * i;
	}

	/**
	 * 计算分数
	 */
	public void explainScore() {
		this.selfScore = MathUtil.scoreWord(this.name, this.nature);
		this.score = selfScore;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name + "\t" + this.score + "\t" + this.freq + "\t" + this.getNature().termNatures[0];
	}

}
