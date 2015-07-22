package org.ansj.recognition;

import org.ansj.AnsjItem;
import org.ansj.Term;
import org.ansj.TermNature;
import org.ansj.TermNatures;
import org.ansj.AnsjContext;
import org.nlpcn.commons.lang.util.WordAlert;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static java.util.stream.Collectors.toList;
import static org.ansj.AnsjItem.NULL_ITEM;
import static org.ansj.AnsjContext.CONTEXT;

/**
 * 词性标注工具类
 *
 * @author ansj
 */
public class NatureRecognition {

    /**
     * 关于这个term的词性
     *
     * @author ansj
     */
    private static class NatureTerm {

        public final TermNature termNature;

        public final double selfScore;

        public double score = 0;

        public NatureTerm from;

        protected NatureTerm(final TermNature termNature) {
            this.termNature = termNature;
            this.selfScore = termNature.frequency + 1;
        }

        public void setScore(final NatureTerm natureTerm) {
            double tempScore = compuNatureFreq(natureTerm, this);
            if (this.from == null || this.score < tempScore) {
                this.score = tempScore;
                this.from = natureTerm;
            }
        }

        @Override
        public String toString() {
            return this.termNature.nature.natureStr + "/" + this.selfScore;
        }

        /**
         * 两个词性之间的分数计算
         *
         * @param from from
         * @param to   to
         * @return score
         */
        public static double compuNatureFreq(final NatureTerm from, final NatureTerm to) {
            double twoWordFreq = AnsjContext.natureLibrary.getNatureFreq(from.termNature.nature, to.termNature.nature);
            if (twoWordFreq == 0) {
                twoWordFreq = Math.log(from.selfScore + to.selfScore);
            }
            return from.score + Math.log((from.selfScore + to.selfScore) * twoWordFreq) + to.selfScore;
        }
    }

    private final NatureTerm root;

    private final List<NatureTerm> end;

    private final List<Term> terms;

    private final List<List<NatureTerm>> table;

    /**
     * 构造方法.传入分词的最终结果
     *
     * @param terms terms
     */
    public NatureRecognition(final List<Term> terms) {
        this.terms = terms;

        this.root = new NatureTerm(TermNature.BEGIN);
        this.end = newArrayList(new NatureTerm(TermNature.END));
        this.table = newArrayListWithExpectedSize(terms.size() + 1);
        for (final Term term : terms) {
            this.table.add(term.getTermNatures().termNatures.stream().map(NatureTerm::new).collect(toList()));
        }
        this.table.add(end);
    }

    /**
     * 进行最佳词性查找,引用赋值.所以不需要有返回值
     */
    public List<Term> recognition() {
        // walk()
        this.table.get(0).forEach(it -> it.setScore(this.root));
        for (int i = 0; i < this.table.size() - 1; i++) {
            for (int j = 0; j < this.table.get(i).size(); j++) {
                final NatureTerm natureTerm = this.table.get(i).get(j);
                this.table.get(i + 1).forEach(it -> it.setScore(natureTerm));
            }
        }

        // optimalRoot() 获得最优路径
        NatureTerm to = this.end.get(0);
        NatureTerm from;
        int index = this.table.size() - 1;
        while ((from = to.from) != null && index > 0) {
            terms.get(--index).setNature(from.termNature.nature);
            to = from;
        }

        return this.terms;
    }

    /**
     * 传入一组。词对词语进行。词性标注
     *
     * @param words words
     * @param offe  offe
     * @return terms
     */
    public static List<Term> recognition(final List<String> words, final int offe) {
        final List<Term> terms = newArrayListWithCapacity(words.size());

        int tempOffe = 0;
        for (final String word : words) {
            // 获得词性, 先从系统辞典, 再从用户自定义辞典
            final AnsjItem ansjItem = CONTEXT().coreDictionary.getItem(word);
            final TermNatures tn;
            if (ansjItem != NULL_ITEM) {
                tn = ansjItem.termNatures;
            } else {
                final String[] params = CONTEXT().getUserLibrary().getParams(word);
                if (params != null) {
                    tn = new TermNatures(new TermNature(params[0], 1));
                } else if (WordAlert.isEnglish(word)) {
                    tn = TermNatures.EN;
                } else if (WordAlert.isNumber(word)) {
                    tn = TermNatures.M;
                } else {
                    tn = TermNatures.NULL;
                }
            }

            terms.add(new Term(word, offe + tempOffe, tn));
            tempOffe += word.length();
        }

        return new NatureRecognition(terms).recognition();
    }

//    /**
//     * 传入一组词, 对词语进行词性标注
//     *
//     * @param words words
//     * @return terms
//     */
//    public static List<Term> recognition(final List<String> words) {
//        return recognition(words, 0);
//    }
}
