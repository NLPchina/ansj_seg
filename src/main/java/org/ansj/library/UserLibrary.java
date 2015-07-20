package org.ansj.library;

import lombok.Getter;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Branch;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.domain.WoodInterface;
import org.nlpcn.commons.lang.tire.library.Library;


/**
 * 用户自定义词典操作类
 *
 * @author ansj
 */
public final class UserLibrary {

    @Getter
    private final Forest forest;

    @Getter
    private final Forest ambiguityForest;

    // 加载用户自定义词典和补充词典
    public UserLibrary(final Forest forest, final Forest ambiguityForest) {
        this.forest = forest != null ? forest : new Forest();
        this.ambiguityForest = ambiguityForest != null ? ambiguityForest : new Forest();
    }

    public GetWord getWord(final char[] chars) {
        return new GetWord(this.ambiguityForest, chars);
    }

    /**
     * 关键词增加
     *
     * @param word   所要增加的关键词
     * @param nature 关键词的词性
     * @param freq   关键词的词频
     */
    @Deprecated
    public void insertWord(final String word, final String nature, final int freq) {
        Library.insertWord(this.forest, new Value(word, nature, String.valueOf(freq)));
    }

    /**
     * 删除关键词
     */
    @Deprecated
    public void removeWord(final String word) {
        Library.removeWord(this.forest, word);
    }

    /**
     * 将用户自定义词典清空
     */
    @Deprecated
    public void clear() {
        this.forest.clear();
    }

    public String[] getParams(final String word) {
        return getParams(this.forest, word);
    }

    @Deprecated
    public boolean contains(final String word) {
        return this.getParams(word) != null;
    }

    public static String[] getParams(final WoodInterface<String[], Branch> forest, final String word) {
        WoodInterface<String[], Branch> pointer = forest;
        for (final char ch : word.toCharArray()) {
            pointer = pointer.getBranch(ch);
            if (pointer == null) {
                return null;
            }
        }
        return pointer.getStatus() > 1 ? pointer.getParam() : null;
    }
}
