package org.ansj.recognition;

import org.ansj.domain.AnsjItem;
import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.library.DATDictionary;
import org.ansj.library.UserDefineLibrary;
import org.ansj.util.MathUtil;
import org.nlpcn.commons.lang.util.WordAlert;

import java.util.ArrayList;
import java.util.List;

/**
 * 词性标注工具类
 *
 * @author ansj
 */
public class NatureRecognition {

    private final NatureTerm root;

    private final NatureTerm[] end;

    private final List<Term> terms;

    private final NatureTerm[][] natureTermTable;

    /**
     * 构造方法.传入分词的最终结果
     *
     * @param terms
     */
    public NatureRecognition(final List<Term> terms) {
        this.root = new NatureTerm(TermNature.BEGIN);
        this.end = new NatureTerm[]{new NatureTerm(TermNature.END)};

        this.terms = terms;
        this.natureTermTable = new NatureTerm[terms.size() + 1][];
        this.natureTermTable[terms.size()] = end;
    }

    /**
     * 进行最佳词性查找,引用赋值.所以不需要有返回值
     */
    public void recognition() {
        for (int i = 0; i < this.terms.size(); i++) {
            this.natureTermTable[i] = getNatureTermArr(this.terms.get(i).termNatures().termNatures);
        }
        walk();
    }

    /**
     * 传入一组。词对词语进行。词性标注
     *
     * @param words words
     * @param offe  offe
     * @return terms
     */
    public static List<Term> recognition(final List<String> words, final int offe) {
        final List<Term> terms = new ArrayList<>(words.size());

        String[] params;
        int tempOffe = 0;
        for (final String word : words) {
            // 获得词性, 先从系统辞典, 再从用户自定义辞典
            final AnsjItem ansjItem = DATDictionary.getItem(word);

            final TermNatures tn;
            if (ansjItem.termNatures != TermNatures.NULL) {
                tn = ansjItem.termNatures;
            } else if ((params = UserDefineLibrary.getInstance().getParams(word)) != null) {
                tn = new TermNatures(new TermNature(params[0], 1));
            } else if (WordAlert.isEnglish(word)) {
                tn = TermNatures.EN;
            } else if (WordAlert.isNumber(word)) {
                tn = TermNatures.M;
            } else {
                tn = TermNatures.NULL;
            }

            terms.add(new Term(word, offe + tempOffe, tn));
            tempOffe += word.length();
        }
        new NatureRecognition(terms).recognition();
        return terms;
    }

    public void walk() {
        final int length = this.natureTermTable.length - 1;
        setScore(this.root, this.natureTermTable[0]);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < this.natureTermTable[i].length; j++) {
                setScore(this.natureTermTable[i][j], this.natureTermTable[i + 1]);
            }
        }
        optimalRoot();
    }

    private void setScore(final NatureTerm natureTerm, final NatureTerm[] natureTerms) {
        for (final NatureTerm nt : natureTerms) {
            nt.setScore(natureTerm);
        }
    }

    private NatureTerm[] getNatureTermArr(final TermNature[] termNatures) {
        final NatureTerm[] natureTerms = new NatureTerm[termNatures.length];
        for (int i = 0; i < natureTerms.length; i++) {
            natureTerms[i] = new NatureTerm(termNatures[i]);
        }
        return natureTerms;
    }

    /**
     * 获得最优路径
     */
    private void optimalRoot() {
        NatureTerm from;
        NatureTerm to = end[0];
        int index = natureTermTable.length - 1;
        while ((from = to.from) != null && index > 0) {
            terms.get(--index).setNature(from.termNature.nature);
            to = from;
        }
    }

    /**
     * 关于这个term的词性
     *
     * @author ansj
     */
    public class NatureTerm {

        public final TermNature termNature;

        public final double selfScore;

        public double score = 0;

        public NatureTerm from;

        protected NatureTerm(final TermNature termNature) {
            this.termNature = termNature;
            this.selfScore = termNature.frequency + 1;
        }

        public void setScore(final NatureTerm natureTerm) {
            double tempScore = MathUtil.compuNatureFreq(natureTerm, this);
            if (this.from == null || this.score < tempScore) {
                this.score = tempScore;
                this.from = natureTerm;
            }
        }

        @Override
        public String toString() {
            return this.termNature.nature.natureStr + "/" + this.selfScore;
        }
    }
}
