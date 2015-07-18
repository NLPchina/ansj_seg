package org.ansj.domain;

import lombok.Getter;
import lombok.Setter;
import org.ansj.util.MathUtil;

import java.util.List;

import static org.ansj.util.MyStaticValue.NATURE_NULL;

public class Term implements Comparable<Term> {

    @Setter
    @Getter
    private String name;// 当前词
    @Setter
    @Getter
    private String realName;
    @Setter
    @Getter
    private int offe;// 当前词的起始位置
    @Getter//这个term的所有词性
    private TermNatures termNatures = TermNatures.NULL;// 词性列表
    @Getter
    private AnsjItem item = AnsjItem.NULL;// 词性列表
    @Getter
    private Term next;// 同一行内数据
    @Setter
    @Getter
    private double score = 0;// 分数
    @Setter
    @Getter
    private double selfScore = 1;// 本身分数
    @Setter
    @Getter
    private Term from;// 起始位置
    @Setter
    @Getter
    private Term to;// 到达位置
    @Setter
    @Getter//获得这个词的词性.词性计算后才可生效
    private Nature nature = NATURE_NULL();// 本身这个term的词性.需要在词性识别之后才会有值,默认是空
    @Setter
    @Getter
    private List<Term> subTerm = null;

    public Term(final String name, final int offe, final AnsjItem item) {
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

    public Term(final String name, final int offe, final TermNatures termNatures) {
        super();
        this.name = name;
        this.offe = offe;
        this.termNatures = termNatures;
        if (termNatures.nature != null) {
            this.nature = termNatures.nature;
        }
    }

    public Term(final String name, final int offe, final String natureStr, final int natureFreq) {
        super();
        this.name = name;
        this.offe = offe;
        TermNature termNature = new TermNature(natureStr, natureFreq);
        this.nature = termNature.nature;
        this.termNatures = new TermNatures(termNature);
    }

    /**
     * 返回他自己
     *
     * @param next 设置他的下一个
     * @return
     */
    public Term setNext(Term next) {
        this.next = next;
        return this;
    }

    // 可以到达的位置
    public int toValue() {
        return this.offe + this.name.length();
    }

    /**
     * 核心构建最优的路径
     *
     * @param from
     */
    public void setPathScore(final Term from) {
        // 维特比进行最优路径的构建
        double score = MathUtil.compuScore(from, this);
        if (this.from == null || this.score >= score) {
            this.setFromAndScore(from, score);
        }
    }

    /**
     * 核心分数的最优的路径,越小越好
     *
     * @param from
     */
    public void setPathSelfScore(final Term from) {
        double score = this.selfScore + from.score;
        // 维特比进行最优路径的构建
        if (this.from == null || this.score > score) {
            this.setFromAndScore(from, score);
        }
    }

    private void setFromAndScore(final Term from, final double score) {
        this.from = from;
        this.score = score;
    }

    /**
     * 更新偏移量
     *
     * @param offe
     */
    public void updateOffe(int offe) {
        this.offe += offe;
    }

    /**
     * 将term的所有分数置为0
     */
    public void clearScore() {
        this.score = 0;
        this.selfScore = 0;
    }

    @Override
    public int compareTo(final Term o) {
        return this.score > o.score ? 0 : 1;
    }

    @Override
    public String toString() {
        return "null".equals(this.nature.natureStr) ?
                this.name :
                (this.realName != null ? this.realName : this.name) + "/" + this.nature.natureStr;
    }
}