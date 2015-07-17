package org.ansj.app.summary;

import org.ansj.app.keyword.Keyword;
import org.nlpcn.commons.lang.tire.SmartGetWord;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

import java.util.List;

/**
 * 关键字标红，
 *
 * @author ansj
 */
public class TagContent {

    private String beginTag, endTag;

    public TagContent(final String beginTag, final String endTag) {
        this.beginTag = beginTag;
        this.endTag = endTag;
    }

    public String tagContent(final Summary summary) {
        return tagContent(summary.getKeyWords(), summary.getSummary());
    }

    public String tagContent(final List<Keyword> keyWords, final String content) {
        final SmartForest<Double> sf = new SmartForest<>();
        for (final Keyword keyWord : keyWords) {
            sf.addBranch(keyWord.getName().toLowerCase(), keyWord.getScore());
        }

        final SmartGetWord<Double> sgw = new SmartGetWord<>(sf, content.toLowerCase());

        final StringBuilder sb = new StringBuilder();
        String temp;
        int beginOffe = 0;
        while ((temp = sgw.getFrontWords()) != null) {
            sb.append(content.substring(beginOffe, sgw.offe));
            sb.append(beginTag);
            sb.append(content.substring(sgw.offe, sgw.offe + temp.length()));
            sb.append(endTag);
            beginOffe = sgw.offe + temp.length();
        }

        if (beginOffe < content.length() - 1) {
            sb.append(content.substring(beginOffe, content.length()));
        }

        return sb.toString();
    }
}
