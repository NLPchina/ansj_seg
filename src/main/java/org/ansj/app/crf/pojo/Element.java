package org.ansj.app.crf.pojo;

import lombok.Getter;
import org.ansj.app.crf.Model;

public class Element {

    private static final double MIN = Integer.MIN_VALUE;

    public final char name;
    @Getter
    private int tag = -1;

    public int len = 1;

    public Element(final char name) {
        this.name = name;
    }

    public Element updateTag(final int tag) {
        this.tag = tag;
        return this;
    }

    public void len() {
        this.len++;
    }

    public double[] tagScore;

    public int[] from;

    public void maxFrom(final Model model, final Element element) {
        if (this.from == null) {
            this.from = new int[this.tagScore.length];
        }
        final double[] pTagScore = element.tagScore;
        for (int i = 0; i < this.tagScore.length; i++) {
            double maxValue = MIN;
            for (int j = 0; j < pTagScore.length; j++) {
                double rate = model.tagRate(j, i);
                if (rate != Double.MIN_VALUE) {
                    final double value = (pTagScore[j] + this.tagScore[i]) + rate;
                    if (value > maxValue) {
                        maxValue = value;
                        this.from[i] = j;
                    }
                }
            }
            this.tagScore[i] = maxValue;
        }
    }

    @Override
    public String toString() {
        return this.name + "/" + this.tag;
    }

//	public Element(final Character name, final int tag) {
//		this.name = name;
//		this.tag = tag;
//	}
//
//    private String nature;
//
//	public Element updateNature(String nature) {
//		this.nature = nature;
//		return this;
//	}
//
//	public static char getTagName(final int tag) {
//		switch (tag) {
//		case 0:
//			return 'S';
//		case 1:
//			return 'B';
//		case 2:
//			return 'M';
//		case 3:
//			return 'E';
//		default:
//			return '?';
//		}
//	}
}
