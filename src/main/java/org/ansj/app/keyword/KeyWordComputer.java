package org.ansj.app.keyword;

import com.google.common.collect.ImmutableMap;
import org.ansj.domain.Term;

import java.util.*;

import static org.ansj.splitWord.NlpAnalysis.nlpParse;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class KeyWordComputer {

    private static final Map<String, Double> POS_SCORE;

    static {
        final Map<String, Double> MAP = new HashMap<>();
        MAP.put("null", 0.0);
        MAP.put("w", 0.0);
        MAP.put("en", 0.0);
        MAP.put("num", 0.0);
        MAP.put("nr", 3.0);
        MAP.put("nrf", 3.0);
        MAP.put("nw", 3.0);
        MAP.put("nt", 3.0);
        MAP.put("l", 0.2);
        MAP.put("a", 0.2);
        MAP.put("nz", 3.0);
        MAP.put("v", 0.2);
        POS_SCORE = ImmutableMap.copyOf(MAP);
    }

    private final int nKeyword;

    public KeyWordComputer() {
        this.nKeyword = 5;
    }

    /**
     * @param nKeyword 返回关键词个数
     */
    public KeyWordComputer(final int nKeyword) {
        this.nKeyword = nKeyword;
    }

    /**
     * @param content 正文
     */
    private List<Keyword> computeArticleTfidf(final String content, final int titleLength) {
        final Map<String, Keyword> termMap = new HashMap<>();

        for (final Term term : nlpParse(content)) {
            final double weight = getWeight(term, content.length(), titleLength);
            if (weight != 0) {
                final Keyword keyword = termMap.get(term.getName());
                if (keyword != null) {
                    keyword.updateWeight(1);
                } else {
                    termMap.put(term.getName(), new Keyword(term.getName(), term.getNature().allFrequency, weight));
                }
            }
        }

        final ArrayList<Keyword> arrayList = new ArrayList<>(new TreeSet<>(termMap.values()));
        return arrayList.size() <= this.nKeyword ? arrayList : arrayList.subList(0, this.nKeyword);
    }

    /**
     * @param title   标题
     * @param content 正文
     */
    public List<Keyword> computeArticleTfidf(final String title, final String content) {
        final String t = isNotBlank(title) ? title : "";
        final String c = isNotBlank(content) ? content : "";
        return computeArticleTfidf(t + "\t" + c, t.length());
    }

    /**
     * 只有正文
     */
    public List<Keyword> computeArticleTfidf(final String content) {
        return computeArticleTfidf(content, 0);
    }

    private double getWeight(final Term term, final int length, final int titleLength) {
        if (term.getName().trim().length() < 2) {
            return 0;
        }

        final String pos = term.getNature().natureStr;
        final Double posScore = POS_SCORE.get(pos) != null ? POS_SCORE.get(pos) : 1.0;
        if (posScore == 0) {
            return 0;
        } else if (titleLength > term.getOffe()) {
            return 5 * posScore;
        } else {
            return (length - term.getOffe()) * posScore / (double) length;
        }
    }
}
