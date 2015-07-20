package org.ansj.app.crf.pojo;

import org.ansj.app.crf.Model;
import org.apache.commons.lang3.ArrayUtils;

public class Element {

    private static final double MIN = Integer.MIN_VALUE;

    public final char name;
    public final int tag;
    public final int len;

    private final double[] tagScore;
    public int[] from;

    private Element(final char name, final int tag, final int len, final double[] tagScore, int[] from) {
        this.name = name;
        this.tag = tag;
        this.len = len;
        this.tagScore = tagScore;
        this.from = from;
    }

    public Element(final char name) {
        this(name, -1, 1, null, null);
    }

    public Element withTag(final int tag) {
        return new Element(this.name, tag, this.len, this.tagScore, this.from);
    }

    public Element withLenPlusOne() {
        return new Element(this.name, this.tag, this.len + 1, this.tagScore, this.from);
    }

    public double tagScore(final int idx) {
        return this.tagScore[idx];
    }

    public Element withTagScore(final int idx, final double newVal) {
        final double[] tagScore = ArrayUtils.clone(this.tagScore);
        tagScore[idx] = newVal;
        return withTagScore(tagScore);
    }

    public Element withTagScore(final double[] tagScore) {
        return new Element(this.name, this.tag, this.len, tagScore, this.from);
    }

    public Element maxFrom(final Model model, final Element element) {
        final double[] tagScore = this.tagScore;//ArrayUtils.clone(this.tagScore);
        final int[] from = this.from != null ? ArrayUtils.clone(this.from) : new int[this.tagScore.length];
        final double[] pTagScore = element.tagScore;
        for (int i = 0; i < tagScore.length; i++) {
            double maxValue = MIN;
            for (int j = 0; j < pTagScore.length; j++) {
                final double rate = model.tagRate(j, i);
                if (rate != Double.MIN_VALUE) {
                    final double value = (pTagScore[j] + tagScore[i]) + rate;
                    if (value > maxValue) {
                        maxValue = value;
                        from[i] = j;
                    }
                }
            }
            tagScore[i] = maxValue;
        }
        return new Element(this.name, this.tag, this.len, tagScore, from);
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
