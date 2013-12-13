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

    public NewWord(String name, TermNatures nature, double score, int freq) {
        this.name = name;
        this.nature = nature;
        this.score = score;
        this.allFreq = freq;
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
     * 更新发现权重,并且更新词性
     * 
     * @param version
     * @param i
     * @param tn
     */
    public void update(double score, TermNatures tn, int freq) {
        // TODO Auto-generated method stub
        this.score += score * freq;
        this.allFreq += freq;
        if (TermNatures.NW.equals(this.nature) || !TermNatures.NW.equals(tn)) {
            this.nature = tn;
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

    public void setScore(double score) {
        // TODO Auto-generated method stub
        this.score = score;
    }

}
