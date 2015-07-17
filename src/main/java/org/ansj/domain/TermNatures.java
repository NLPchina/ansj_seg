package org.ansj.domain;

import static org.ansj.domain.NumNatureAttr.NULL_NUM_NATURE_ATTR;
import static org.ansj.domain.PersonNatureAttr.NULL_PERSON_NATURE_ATTR;

/**
 * 没一个term都拥有一个词性集合
 *
 * @author ansj
 */
public class TermNatures {

    public static final TermNatures M = new TermNatures(TermNature.M);

    public static final TermNatures NR = new TermNatures(TermNature.NR);

    public static final TermNatures EN = new TermNatures(TermNature.EN);

    public static final TermNatures END = new TermNatures(-1, TermNature.END.withFrequency(50610));

    public static final TermNatures BEGIN = new TermNatures(0, TermNature.BEGIN.withFrequency(50610));

    public static final TermNatures NT = new TermNatures(TermNature.NT);

    public static final TermNatures NW = new TermNatures(TermNature.NW);

    public static final TermNatures NULL = new TermNatures(TermNature.NULL);
    ;

    /**
     * 词的id
     */
    public final int id;
    /**
     * 关于这个term的所有词性
     */
    public final TermNature[] termNatures;
    /**
     * 所有的词频
     */
    public final int allFreq;
    /**
     * 默认词性
     */
    public final Nature nature;
    /**
     * 数字属性
     */
    public final NumNatureAttr numAttr;
    /**
     * 人名词性
     */
    public final PersonNatureAttr personAttr;

    /**
     * 构造方法.一个词对应这种玩意
     */
    public TermNatures(final int id, final TermNature... termNatures) {
        this(
                id,
                termNatures,
                totalFreq(termNatures),
                natureOfMaxFreq(termNatures),
                numNatureAttr(termNatures),
                NULL_PERSON_NATURE_ATTR
        );
    }

    public TermNatures(final TermNature termNature) {
        this(-2, termNature);
//        this(
//                -2,
//                new TermNature[]{termNature},
//                totalFreq(new TermNature[]{termNature}),
//                termNature.nature,
//                numNatureAttr(new TermNature[]{termNature}),
//                NULL_PERSON_NATURE_ATTR
//        );
    }

    private TermNatures(
            final int id,
            final TermNature[] termNatures,
            final int allFreq,
            final Nature nature,
            final NumNatureAttr numAttr,
            final PersonNatureAttr personAttr
    ) {
        this.id = id;
        this.termNatures = termNatures;
        this.allFreq = allFreq;
        this.nature = nature;
        this.numAttr = numAttr != null ? numAttr : NULL_NUM_NATURE_ATTR;
        this.personAttr = personAttr != null ? personAttr : NULL_PERSON_NATURE_ATTR;
    }

    public TermNatures withNumAttr(final NumNatureAttr numAttr) {
        return new TermNatures(this.id, this.termNatures, this.allFreq, this.nature, numAttr, this.personAttr);
    }

    public TermNatures withPersonAttr(final PersonNatureAttr personAttr) {
        return new TermNatures(this.id, this.termNatures, this.allFreq, this.nature, this.numAttr, personAttr);
    }

    // find maxNature
    private static Nature natureOfMaxFreq(final TermNature[] termNatures) {
        TermNature termNatureOfMaxFreq = null;
        int maxFreq = -1;
        for (final TermNature nature : termNatures) {
            if (maxFreq < nature.frequency) {
                maxFreq = nature.frequency;
                termNatureOfMaxFreq = nature;
            }
        }
        return termNatureOfMaxFreq != null ? termNatureOfMaxFreq.nature : null;
    }

    private static int totalFreq(final TermNature[] termNatures) {
        int totalFreq = 0;
        for (final TermNature termNature : termNatures) {
            totalFreq += termNature.frequency;
        }
        return totalFreq;
    }

    private static NumNatureAttr numNatureAttr(final TermNature[] termNatures) {
        int maxFreq = 0;
        NumNatureAttr numAttr = null;
        for (final TermNature termNature : termNatures) {
            maxFreq = Math.max(maxFreq, termNature.frequency);
            switch (termNature.nature.index) {
                case 18:
                    numAttr = numAttr == null ? NULL_NUM_NATURE_ATTR : numAttr.withNumFreq(termNature.frequency);
                    break;
                case 29:
                    numAttr = numAttr == null ? NULL_NUM_NATURE_ATTR : numAttr.withNumEndFreq(termNature.frequency);
                    break;
            }
        }
        if (numAttr != null) {
            if (maxFreq == numAttr.numFreq) {
                numAttr = numAttr.withFlag(true);
            }
            return numAttr;
        } else {
            return null;
        }
    }
}
