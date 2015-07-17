package org.ansj.app.summary;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.tire.SmartGetWord;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自动摘要,同时返回关键词
 *
 * @author ansj
 */
public class SummaryComputer {

    private static final Set<String> FILTER_SET = new HashSet<>();

    static {
        FILTER_SET.add("w");
        FILTER_SET.add("null");
    }

    final String title, content;
    /**
     * summaryLength
     */
    private final int len;

    private final boolean isSplitSummary;

    public SummaryComputer(final String title, final String content) {
        this(title, content, 300, true);
    }

    public SummaryComputer(final int len, final String title, final String content) {
        this(title, content, len, true);
    }

    private SummaryComputer(final String title, final String content, final int len, final boolean isSplitSummary) {
        this.len = len;
        this.title = title;
        this.content = content;
        this.isSplitSummary = isSplitSummary;
    }

    /**
     * 计算摘要，利用关键词抽取计算
     */
    public Summary toSummary() {
        return toSummary(new ArrayList<>());
    }

    /**
     * 根据用户查询串计算摘要
     */
    public Summary toSummary(final String query) {
        final List<Keyword> keywords = NlpAnalysis.nlpParse(query)
                .stream()
                .filter(term -> !FILTER_SET.contains(term.natrue().natureStr))
                .map(term -> new Keyword(term.getName(), term.termNatures().allFreq, 1))
                .collect(Collectors.toList());
        return toSummary(keywords);
    }

    /**
     * 计算摘要，传入用户自己算好的关键词
     */
    public Summary toSummary(final List<Keyword> keywords) {
        if (keywords == null || keywords.size() == 0) {
            return explan(new KeyWordComputer(10).computeArticleTfidf(this.title, this.content), this.content);
        } else {
            return explan(keywords, this.content);
        }
    }

    /**
     * 计算摘要
     *
     * @param keywords keywords
     * @param content  content
     * @return summary
     */
    private Summary explan(final List<Keyword> keywords, final String content) {
        final SmartForest<Double> sf = new SmartForest<>();
        for (final Keyword keyword : keywords) {
            sf.addBranch(keyword.getName(), keyword.getScore());
        }

        // 先断句
        final List<Sentence> sentences = toSentenceList(content.toCharArray());
        for (final Sentence sentence : sentences) {
            computeScore(sentence, sf);
        }

        double maxScore = 0;
        int maxIndex = 0;
        for (int i = 0; i < sentences.size(); i++) {
            double tempScore = sentences.get(i).score;
            int tempLength = sentences.get(i).value.length();

            if (tempLength >= this.len) {
                if (maxScore < tempScore) {
                    maxScore = tempScore;
                    maxIndex = i;
                    continue;
                }
            }
            for (int j = i + 1; j < sentences.size(); j++) {
                tempScore += sentences.get(j).score;
                tempLength += sentences.get(j).value.length();
                if (tempLength >= this.len) {
                    if (maxScore < tempScore) {
                        maxScore = tempScore;
                        maxIndex = i;
                        break;
                    }
                }
            }

            if (tempLength < this.len) {
                if (maxScore < tempScore) {
                    maxScore = tempScore;
                    maxIndex = i;
                    break;
                }
            } else {
                break;
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = maxIndex; i < sentences.size(); i++) {
            sb.append(sentences.get(i).value);
            if (sb.length() > this.len) {
                break;
            }
        }
        String summaryStr = sb.toString();

        /**
         * 是否强制文本长度。对于abc这种字符算半个长度
         */
        if (this.isSplitSummary && sb.length() > this.len) {
            double value = this.len;

            final StringBuilder newSummary = new StringBuilder();
            for (int i = 0; i < sb.length(); i++) {
                final char c = sb.charAt(i);
                if (c < 256) {
                    value -= 0.5;
                } else {
                    value -= 1;
                }
                if (value < 0) {
                    break;
                }
                newSummary.append(c);
            }
            summaryStr = newSummary.toString();
        }

        return new Summary(keywords, summaryStr);
    }

    /**
     * 计算一个句子的分数
     *
     * @param sentence sentence
     * @param forest   forest
     */
    private void computeScore(Sentence sentence, SmartForest<Double> forest) {
        SmartGetWord<Double> sgw = new SmartGetWord<>(forest, sentence.value);
        while (sgw.getFrontWords() != null) {
            sentence.score += sgw.getParam();
        }
        if (sentence.score == 0) {
            sentence.score = sentence.value.length() * -0.005;
        } else {
            sentence.score /= Math.log(sentence.value.length() + 3);
        }
    }

    public List<Sentence> toSentenceList(final char[] chars) {
        StringBuilder sb = new StringBuilder();
        final List<Sentence> sentences = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (sb.length() == 0 && (Character.isWhitespace(chars[i]) || chars[i] == ' ')) {
                continue;
            }

            sb.append(chars[i]);
            switch (chars[i]) {
                case '.':
                    if (i < chars.length - 1 && chars[i + 1] > 128) {
                        insertIntoList(sb, sentences);
                        sb = new StringBuilder();
                    }
                    break;
                case ' ':
                case '	':
                case ' ':
                case '。':
                    insertIntoList(sb, sentences);
                    sb = new StringBuilder();
                    break;
                case ';':
                case '；':
                    insertIntoList(sb, sentences);
                    sb = new StringBuilder();
                    break;
                case '!':
                case '！':
                    insertIntoList(sb, sentences);
                    sb = new StringBuilder();
                    break;
                case '?':
                case '？':
                    insertIntoList(sb, sentences);
                    sb = new StringBuilder();
                    break;
                case '\n':
                case '\r':
                    insertIntoList(sb, sentences);
                    sb = new StringBuilder();
                    break;
            }
        }

        if (sb.length() > 0) {
            insertIntoList(sb, sentences);
        }
        return sentences;
    }

    private void insertIntoList(final StringBuilder sb, final List<Sentence> sentences) {
        final String content = sb.toString().trim();
        if (content.length() > 0) {
            sentences.add(new Sentence(content));
        }
    }

    /*
     * 句子对象
     */
    class Sentence {
        String value;
        double score;

        public Sentence(String value) {
            this.value = value.trim();
        }

        public String toString() {
            return value;
        }
    }
}
