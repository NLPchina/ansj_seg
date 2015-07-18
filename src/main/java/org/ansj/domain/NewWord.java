package org.ansj.domain;

import lombok.Getter;
import lombok.Setter;

import static org.ansj.util.MyStaticValue.NATURE_NW;
import static org.ansj.util.MyStaticValue.TAB;

/**
 * 新词发现,实体名
 *
 * @author ansj
 */
@Getter
public class NewWord {
    // 名字
    private final String name;
    // 词性
    private Nature nature;
    // 分数
    @Setter
    private double score;
    // 总词频
    private int allFreq;
    //此词是否被激活
    @Setter
    private boolean isActive;

    public NewWord(final String name, final Nature nature, final double score) {
        this.name = name;
        this.nature = nature;
        this.score = score;
        this.allFreq = 1;
    }

    public NewWord(final String name, final Nature nature) {
        this(name, nature, 0);
    }

    /**
     * 更新发现权重,并且更新词性
     *
     * @param nature nature
     * @param freq   freq
     */
    public void update(final Nature nature, final int freq) {
        this.score += this.score * freq;
        this.allFreq += freq;
        if (NATURE_NW() != nature) {
            this.nature = nature;
        }
    }

    @Override
    public String toString() {
        return this.name + TAB + this.score + TAB + this.getNature().natureStr;
    }
}
