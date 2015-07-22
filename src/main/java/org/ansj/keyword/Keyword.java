package org.ansj.keyword;

import lombok.Getter;

public class Keyword implements Comparable<Keyword> {

    //@Setter
    @Getter
    private final String name;
    @Getter
    private double score;
    private final double idf;
    @Getter
    private int freq;

    public Keyword(final String name, final int docFreq, final double weight) {
        this.name = name;
        this.idf = Math.log(10000 + 10000.0 / (docFreq + 1));
        this.score = this.idf * weight;
        this.freq++;
    }

    public Keyword(final String name, final double score) {
        this.name = name;
        this.score = score;
        this.idf = score;
        this.freq++;
    }

    public void updateWeight(final int weight) { // TODO 改成withWeight, 使score和freq成为不变量
        this.score += weight * this.idf;
        this.freq++;
    }

    @Override
    public int compareTo(final Keyword o) {
        return this.score < o.score ? 1 : -1;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof Keyword) && ((Keyword) obj).name.equals(this.name);
    }

    @Override
    public String toString() {
        return this.name + "/" + this.score;// "="+score+":"+freq+":"+idf;
    }
}
