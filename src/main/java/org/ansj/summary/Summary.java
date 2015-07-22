package org.ansj.summary;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.ansj.keyword.Keyword;

import java.util.List;

/**
 * 摘要结构体封装
 *
 * @author ansj
 */
@Getter
public class Summary {

    /**
     * 关键词
     */
    private final List<Keyword> keyWords;

    /**
     * 摘要
     */
    private final String summary;

    public Summary(final List<Keyword> keyWords, final String summary) {
        this.keyWords = ImmutableList.copyOf(keyWords);
        this.summary = summary;
    }
}
