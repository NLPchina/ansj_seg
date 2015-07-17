package org.ansj.domain;

/**
 * 没一个term都拥有一个词性集合
 *
 * @author ansj
 *
 */
public class TermNatures {

    public static final TermNatures M = new TermNatures(TermNature.M);

    public static final TermNatures NR = new TermNatures(TermNature.NR);

    public static final TermNatures EN = new TermNatures(TermNature.EN);

    public static final TermNatures END = new TermNatures(TermNature.END, 50610, -1);

    public static final TermNatures BEGIN = new TermNatures(TermNature.BEGIN, 50610, 0);

    public static final TermNatures NT = new TermNatures(TermNature.NT);

    public static final TermNatures NW = new TermNatures(TermNature.NW);

    public static final TermNatures NULL = new TermNatures(TermNature.NULL);;

    /**
     * 关于这个term的所有词性
     */
    public TermNature[] termNatures = null;

    /**
     * 数字属性
     */
    public NumNatureAttr numAttr = NumNatureAttr.NULL_NUM_NATURE_ATTR;

    /**
     * 人名词性
     */
    public PersonNatureAttr personAttr = PersonNatureAttr.NULL_PERSON_NATURE_ATTR;

    /**
     * 默认词性
     */
    public Nature nature = null;

    /**
     * 所有的词频
     */
    public int allFreq = 0;

    /**
     * 词的id
     */
    public int id = -2;

    /**
     * 构造方法.一个词对应这种玩意
     *
     * @param termNatures
     */
    public TermNatures(TermNature[] termNatures, int id) {
        this.id = id;
        this.termNatures = termNatures;
        // find maxNature
        int maxFreq = -1;
        TermNature termNature = null;
        for (int i = 0; i < termNatures.length; i++) {
            if (maxFreq < termNatures[i].frequency) {
                maxFreq = termNatures[i].frequency;
                termNature = termNatures[i];
            }
        }

        if (termNature != null) {
            this.nature = termNature.nature;
        }

        serAttribute();
    }

    public TermNatures(TermNature termNature) {
        termNatures = new TermNature[1];
        this.termNatures[0] = termNature;
        this.nature = termNature.nature;
        serAttribute();
    }

    public TermNatures(TermNature termNature, int allFreq, int id) {
        this.id = id;
        termNatures = new TermNature[1];
        termNature.frequency = allFreq;
        this.termNatures[0] = termNature;
        this.allFreq = allFreq;
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
        this.numAttr = numAttr;
        this.personAttr = personAttr;
    }

    public TermNatures withNumAttr(final NumNatureAttr numAttr) {
        return new TermNatures(this.id, this.termNatures, this.allFreq, this.nature, numAttr, this.personAttr);
    }

    public TermNatures withPersonAttr(final PersonNatureAttr personAttr) {
        return new TermNatures(this.id, this.termNatures, this.allFreq, this.nature, this.numAttr, personAttr);
    }

    private void serAttribute() {
        int max = 0;
        NumNatureAttr numAttr = null;
        for (final TermNature termNature : termNatures) {
            allFreq += termNature.frequency;
            max = Math.max(max, termNature.frequency);
            switch (termNature.nature.index) {
                case 18:
                    numAttr = numAttr == null ?
                            NumNatureAttr.NULL_NUM_NATURE_ATTR :
                            numAttr.withNumFreq(termNature.frequency);
                    break;
                case 29:
                    numAttr = numAttr == null ?
                            NumNatureAttr.NULL_NUM_NATURE_ATTR :
                            numAttr.withNumEndFreq(termNature.frequency);
                    break;
            }
        }
        if (numAttr != null) {
            if (max == numAttr.numFreq) {
                numAttr = numAttr.withFlag(true);
            }
            this.numAttr = numAttr;
        }
    }

    public void setPersonNatureAttr(final PersonNatureAttr personAttr) {
        this.personAttr = personAttr;
    }
}
