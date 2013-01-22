package org.ansj.domain;

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
	// 总词频
	private int allFreq;
	// 平均分数
	private double averageScore;
	// 此词是否可用
	private boolean isActive = false ;

	public NewWord(String name, TermNatures nature, double score, int freq) {
		this.name = name;
		this.nature = nature;
		this.score = getScore(nature, score);
		this.allFreq = freq;
		averageScore = score;
		if(allFreq>2||averageScore<-0.5){
			isActive = true ;
		}
	}

	/**
	 * 根据词性对分数划分
	 * 
	 * @param nature2
	 * @return
	 */
	private double getScore(TermNatures nature, double score) {
		// TODO Auto-generated method stub
		if (TermNatures.NW.equals(nature)) {
			return score * -1;
		} else if (TermNatures.NR.equals(nature)) {
			return score * 100;
		} else if (TermNatures.NT.equals(nature)) {
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
	public void update(double score, TermNatures tn, int freq) {
		// TODO Auto-generated method stub
		this.score += getScore(tn, score);
		this.allFreq += freq;
		this.averageScore = this.score / freq;
		if (tn == null || !TermNatures.NW.equals(tn)) {
			this.nature = tn;
		}
		
		if(allFreq>2||averageScore<-0.5){
			isActive = true ;
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name + "\t" + this.score + "\t" + this.getNature().termNatures[0];
	}

	public int getAllFreq() {
		return allFreq;
	}

	public double getAverageScore() {
		return averageScore;
	}
	
	

}
