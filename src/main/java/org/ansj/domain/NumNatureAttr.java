package org.ansj.domain;

public class NumNatureAttr {

    public static final NumNatureAttr NULL = new NumNatureAttr(null, null, null);

    // 是有可能是一个数字
    public final Integer numFreq;

    // 数字的结尾
    public final Integer numEndFreq;

    // 最大词性是否是数字
    public final Boolean flag;

    public NumNatureAttr(final Integer numFreq, final Integer numEndFreq, final Boolean flag) {
        this.numFreq = numFreq != null ? numFreq : -1;
        this.numEndFreq = numEndFreq != null ? numEndFreq : -1;
        this.flag = flag != null ? flag : false;
    }

    public NumNatureAttr withNumFreq(final Integer numFreq) {
        return new NumNatureAttr(numFreq, this.numEndFreq, this.flag);
    }

    public NumNatureAttr withNumEndFreq(final Integer numEndFreq) {
        return new NumNatureAttr(this.numFreq, numEndFreq, this.flag);
    }

    public NumNatureAttr withFlag(final Boolean flag) {
        return new NumNatureAttr(this.numFreq, this.numEndFreq, flag);
    }
}
