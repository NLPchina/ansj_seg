package org.ansj.domain;

/**
 * 这里面封装了一些基本的词性.
 *
 * @author ansj
 */
public class Nature {

    // 词性的名称
    public final String natureStr;
    // 词性对照表的位置
    public final int index;
    // 词性的下标值
    public final int natureIndex;
    // 词性的频率
    public final int allFrequency;

    public Nature(final String natureStr, final int index, final int natureIndex, final int allFrequency) {
        this.natureStr = natureStr;
        this.index = index;
        this.natureIndex = natureIndex;
        this.allFrequency = allFrequency;
    }

    public Nature(final String natureStr) {
        this.natureStr = natureStr;
        this.index = 0;
        this.natureIndex = 0;
        this.allFrequency = 0;
    }

    @Override
    public String toString() {
        return this.natureStr + ":" + this.index + ":" + this.natureIndex;
    }
}