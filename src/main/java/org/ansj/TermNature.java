package org.ansj;

import static java.lang.Integer.parseInt;

/**
 * 一个词里面会有一些词性
 *
 * @author ansj
 */
public class TermNature {
    /**
     * 系统内置的几个
     */
    public static final TermNature M = new TermNature("m", 1);
    public static final TermNature EN = new TermNature("en", 1);
    public static final TermNature BEGIN = new TermNature("始##始", 1);
    public static final TermNature END = new TermNature("末##末", 1);
    public static final TermNature USER_DEFINE = new TermNature("userDefine", 1);
    public static final TermNature NR = new TermNature("nr", 1);
    public static final TermNature NT = new TermNature("nt", 1);
    public static final TermNature NW = new TermNature("nw", 1);
    public static final TermNature NULL = new TermNature("null", 1);

    public final Nature nature;

    public final int frequency;

    private TermNature(final Nature nature, final int frequency) {
        this.nature = nature;
        this.frequency = frequency;
    }

    public TermNature(final String natureStr, final int frequency) {
        this(AnsjContext.natureLibrary.getNature(natureStr), frequency);
    }

    public TermNature withFrequency(final int frequency) {
        return new TermNature(this.nature, frequency);
    }

    public static TermNature[] setNatureStrToArray(final String natureStr) {
        final String[] split = natureStr.substring(1, natureStr.length() - 1).split(",");
        final TermNature[] all = new TermNature[split.length];
        for (int i = 0; i < split.length; i++) {
            final String[] strs = split[i].split("=");
            final Integer frequency = parseInt(strs[1]);
            all[i] = new TermNature(strs[0].trim(), frequency);
        }
        return all;
    }

    @Override
    public String toString() {
        return this.nature.natureStr + "/" + this.frequency;
    }
}
