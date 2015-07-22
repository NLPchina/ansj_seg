package org.ansj;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static org.ansj.AnsjItem.NULL_ITEM;
import static org.ansj.AnsjContext.*;

public class Term implements Comparable<Term> {

    @Setter
    @Getter
    private String name;// 当前词
    @Setter
    private String realName;
    @Getter
    private int offe;// 当前词的起始位置
    @Getter//这个term的所有词性
    private final TermNatures termNatures;// 词性列表
    @Getter
    private final AnsjItem item;// 词性列表
    @Setter
    @Getter
    private Term next;// 同一行内下一个数据
    @Setter
    @Getter
    private double score;// 分数
    @Setter
    @Getter
    private double selfScore;// 本身分数
    @Setter
    @Getter
    private Term from;// 起始位置
    @Setter
    @Getter
    private Term to;// 到达位置
    @Setter
    @Getter//获得这个词的词性.词性计算后才可生效
    private Nature nature;// 本身这个term的词性.需要在词性识别之后才会有值,默认是空
    @Setter
    @Getter
    private List<Term> subTerm;

    private Term(final String name, final int offe, final TermNatures termNatures, final AnsjItem item) {
        this.name = name;
        this.offe = offe;
        this.termNatures = termNatures != null ? termNatures : TermNatures.NULL;
        this.nature = this.termNatures != null ? this.termNatures.nature : AnsjContext.natureLibrary.NATURE_NULL();
        this.score = 0;
        this.selfScore = 1;
        this.item = item != null ? item : NULL_ITEM;
    }

    public Term(final String name, final int offe, final TermNatures termNatures) {
        this(name, offe, termNatures, null);
    }

    public Term(final String name, final int offe, final AnsjItem item) {
        this(name, offe, item.termNatures, item);
    }

    public char firstChar() {
        return this.charAt(0);
    }

    public char charAt(final int index) {
        return this.name.charAt(index);
    }

    public int length() {
        return this.name.length();
    }

    public String getRealName() {
        return this.realName != null ? this.realName : this.name;
    }

    // 可以到达的位置
    public int toValue() {
        return this.offe + this.name.length();
    }

    /**
     * 核心构建最优的路径
     */
    public void setPathScore(final Term from) {// 维特比进行最优路径的构建
        final double score = compuScore(from, this);
        if (this.from == null || this.score >= score) {
            this.setFromAndScore(from, score);
        }
    }

    /**
     * 从一个词的词性到另一个词的词的分数
     *
     * @param from 前面的词
     * @param to   后面的词
     * @return 分数
     */
    static double compuScore(final Term from, final Term to) {
        double frequency = from.getTermNatures().allFreq + 1;

        if (frequency < 0) {
            double score = from.getScore() + MAX_FREQUENCE;
            from.setScore(score);
            return score;
        }

        int nTwoWordsFreq = CONTEXT().ngramLibrary.getTwoWordFreq(from, to);
        double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp));

        if (value < 0) {
            value += frequency;
        }
        return from.getScore() + value;
    }

    /**
     * 核心分数的最优的路径,越小越好
     */
    public void setPathSelfScore(final Term from) {// 维特比进行最优路径的构建
        final double score = this.selfScore + from.score;
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
    public void updateOffe(final int offe) {
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
                this.getRealName() + "/" + this.nature.natureStr;
    }

    /**
     * 将一个term插入到链表中的对应位置中,应该是词长由大到小
     */
    public static void insertTerm(final Term[] terms, final Term term) {
        Term temp = terms[term.getOffe()];
        Term last = temp;//插入到最右面
        while ((temp = temp.getNext()) != null) {
            last = temp;
        }
        last.setNext(term);
    }

    public static void termLink(final Term from, final Term to) {
        if (from == null || to == null)
            return;
        from.setTo(to);
        to.setFrom(from);
    }

    /**
     * 从from到to生成subterm
     */
    public static List<Term> subTerms(final Term from, final Term to) {
        final List<Term> subTerms = new ArrayList<>(3);
        Term pointer = from;
        while ((pointer = pointer.getTo()) != to) {
            subTerms.add(pointer);
        }
        return subTerms;
    }
}