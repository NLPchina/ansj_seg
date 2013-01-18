package org.ansj.domain;

import org.ansj.util.MathUtil;
import org.ansj.util.newWordFind.NewTerm;

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
	// 词性
	private TermNatures nature;

	public NewWord(String name, TermNatures nature) {
		this.name = name;
		this.nature = nature;
	}

	public NewWord(String name, TermNatures nature, double score) {
		this.name = name;
		this.nature = nature;
		this.score = getScore(nature, score);
	}

	/**
	 * 根据词性对分数划分
	 * 
	 * @param nature2
	 * @return
	 */
	private double getScore(TermNatures nature, double score) {
		// TODO Auto-generated method stub
		if (TermNature.NW.equals(nature)) {
			return score*-1;
		} else if (TermNature.NR.equals(nature)) {
			return score * 100;
		} else if (TermNature.NT.equals(nature)) {
			return score * 10;
		}
		return score;
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
	 * @param tn
	 */
	public void update(double score, TermNatures tn) {
		// TODO Auto-generated method stub
		this.score += getScore(tn, score);
		if (tn == null || !TermNatures.NW.equals(tn)) {
			this.nature = tn;
		}
	}

	public void update(double score) {
		// TODO Auto-generated method stub
		this.score += score;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name + "\t" + this.score + "\t" + this.getNature().termNatures[0];
	}

}
