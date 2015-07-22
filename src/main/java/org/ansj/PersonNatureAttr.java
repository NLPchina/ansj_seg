package org.ansj;

/**
 * 人名标注pojo类
 *
 * @author ansj
 */
public class PersonNatureAttr {
    // public int B = -1;//0 姓氏
    // public int C = -1;//1 双名的首字
    // public int D = -1;//2 双名的末字
    // public int E = -1;//3 单名
    // public int N = -1; //4任意字
    // public int L = -1;//11 人名的下文
    // public int M = -1;//12 两个中国人名之间的成分
    // public int m = -1;//44 可拆分的姓名
    // String[] parretn = {"BC", "BCD", "BCDE", "BCDEN"}
    // double[] factory = {"BC", "BCD", "BCDE", "BCDEN"}

    public static final PersonNatureAttr NULL_PERSON_NATURE_ATTR = new PersonNatureAttr(null, null, null, null, null, null);

    // 12
    public final Integer begin;
    // 11+12
    public final Integer end;
    public final Integer split;
    public final Integer allFreq;

    // 是否有可能是名字的第一个字
    public final Boolean flag;

    private final int[][] locFreq;

    private PersonNatureAttr(
            final Integer begin, final Integer end, final Integer split,
            final Integer allFreq, final Boolean flag, final int[][] locFreq
    ) {
        this.begin = begin != null ? begin : 0;
        this.end = end != null ? end : 0;
        this.split = split != null ? split : 0;
        this.allFreq = allFreq != null ? allFreq : 0;
        this.flag = flag != null ? flag : false;
        this.locFreq = locFreq;
    }

    /**
     * 得道某一个位置的词频
     *
     * @param length length
     * @param loc    loc
     * @return 位置的词频
     */
    public int getFreq(final int length, final int loc) {
        return this.locFreq != null ? this.locFreq[length > 3 ? 3 : length][loc > 4 ? 4 : loc] : 0;
    }

    /**
     * 设置
     *
     * @param idx  idx
     * @param freq freq
     */
    public PersonNatureAttr addFreq(final int idx, final int freq) {
        final PersonNatureAttr newVal;
        switch (idx) {
            case 11:
                newVal = new PersonNatureAttr(this.begin, this.end + freq, this.split, this.allFreq + freq, this.flag, this.locFreq);
                break;
            case 12:
                newVal = new PersonNatureAttr(this.begin + freq, this.end + freq, this.split, this.allFreq + freq, this.flag, this.locFreq);
                break;
            case 44:
                newVal = new PersonNatureAttr(this.begin, this.end, this.split + freq, this.allFreq + freq, this.flag, this.locFreq);
                break;
            default:
                newVal = this;
                break;
        }
        return newVal;
    }

    /**
     * 词频记录表
     */
    public PersonNatureAttr withNewLocFreq(final int[][] locFreq) {
        for (final int[] anInt : locFreq) {
            if (anInt[0] > 0) {
                return new PersonNatureAttr(this.begin, this.end, this.split, this.allFreq, true, locFreq);
            }
        }
        return new PersonNatureAttr(this.begin, this.end, this.split, this.allFreq, this.flag, locFreq);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("begin=").append(this.begin).append(",")
                .append("end=").append(this.end).append(",")
                .append("split=").append(this.split)
                .toString();
    }
}
