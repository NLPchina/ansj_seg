package org.ansj.app.phrase;

import org.ansj.domain.Term;
import org.nlpcn.commons.lang.util.MapCount;

import java.util.List;

public class Occurrence {

    /**
     * 左邻项
     */
    private MapCount<String> leftTerms = new MapCount<String>();

    /**
     * 右邻项
     */
    private MapCount<String> rightTerms = new MapCount<String>();

    private List<Term> terms;

    private int frequency;

    private double pmi;
    private double leftEntropy;
    private double rightEntropy;
    private double score;

    public Occurrence(List<Term> terms) {
        this.terms = terms;
    }

    public MapCount<String> getLeftTerms() {
        return leftTerms;
    }

    public void addLeftTerm(String term) {
        this.leftTerms.add(term);
    }

    public MapCount<String> getRightTerms() {
        return rightTerms;
    }

    public void addRightTerm(String term) {
        this.rightTerms.add(term);
    }

    public List<Term> getTerms() {
        return terms;
    }

    public int getFrequency() {
        return frequency;
    }

    public void increaseFrequency() {
        ++this.frequency;
    }

    public double getPmi() {
        return pmi;
    }

    public void setPmi(double pmi) {
        this.pmi = pmi;
    }

    public double getLeftEntropy() {
        return leftEntropy;
    }

    public void setLeftEntropy(double leftEntropy) {
        this.leftEntropy = leftEntropy;
    }

    public double getRightEntropy() {
        return rightEntropy;
    }

    public void setRightEntropy(double rightEntropy) {
        this.rightEntropy = rightEntropy;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
