package org.ansj.recognition;

import org.ansj.NewWord;
import org.ansj.PersonNatureAttr;
import org.ansj.Term;
import org.ansj.TermNatures;
import org.ansj.AnsjContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.ansj.AnsjContext.CONTEXT;

/**
 * 人名识别工具类
 *
 * @author ansj
 */
public class AsianPersonRecognition {

    private static final double[] FACTORY = {0.16271366224044456, 0.8060521860870434, 0.031234151672511947};

    // 名称是否有歧异
    // public int B = -1;//0 姓氏
    // public int C = -1;//1 双名的首字
    // public int D = -1;//2 双名的末字
    // public int E = -1;//3 单名
    // public int N = -1; //4任意字
    // public int L = -1;//11 人名的下文
    // public int M = -1;//12 两个中国人名之间的成分
    // public int m = -1;//44 可拆分的姓名
    // double[] factory = {"BC", "BCD", "BCDE"}

    private final Term[] terms;
    private boolean skip;

    public AsianPersonRecognition(final Term[] terms) {
        this.terms = terms;
        this.skip = false;
    }

    public void recognition() {
        _recogntion().forEach(term -> Term.insertTerm(this.terms, term));
    }

    private List<Term> _recogntion() {
        final List<Term> result = new ArrayList<>();
        int beginFreq = 10;
        for (int i = 0; i < terms.length; i++) {
            final Term term = terms[i];
            if (term == null || !term.getTermNatures().personAttr.flag) {
                continue;
            }
            term.setScore(0);
            term.setSelfScore(0);
            for (int j = 2; j > -1; j--) {
                final int freq = term.getTermNatures().personAttr.getFreq(j, 0);
                if ((freq > 10) || (term.getName().length() == 2 && freq > 10)) {
                    final Term tempTerm = nameFind(i, beginFreq, j);
                    if (tempTerm != null) {
                        result.add(tempTerm);
                        // 如果是无争议性识别
                        if (skip) {
                            for (int j2 = i; j2 < tempTerm.toValue(); j2++) {
                                if (terms[j2] != null) {
                                    terms[j2].setScore(0);
                                    terms[j2].setSelfScore(0);
                                }
                            }
                            i = tempTerm.toValue() - 1;
                            break;
                        }
                    }
                }
            }
            beginFreq = term.getTermNatures().personAttr.begin + 1;
        }
        return result;
    }

    /**
     * 人名识别
     *
     * @param offe      offe
     * @param beginFreq beginFreq
     * @param size      size
     */
    private Term nameFind(final int offe, final int beginFreq, final int size) {
        final StringBuilder names = new StringBuilder();
        int undefinite = 0;
        double allFreq = 0;
        Term term = null;
        int off = offe;
        for (int index = 0; off < terms.length; off++) {
            if (terms[off] == null) {// 走到结尾处识别出来一个名字.
                continue;
            }
            term = terms[off];
            final PersonNatureAttr pna = term.getTermNatures().personAttr;
            final int freq = pna.getFreq(size, index);// 在这个长度的这个位置的词频,如果没有可能就干掉,跳出循环
            if (freq == 0) {
                return null;
            }
            if (pna.allFreq > 0) {
                undefinite++;
            }
            names.append(term.getName());
            allFreq += Math.log(term.getTermNatures().allFreq + 1);
            allFreq += -Math.log((freq));

            index++;
            if (index == size + 2) {
                break;
            }
        }

        double score = -Math.log(FACTORY[size]);
        score += allFreq;
        double endFreq = 0;
        boolean flag = true;
        while (flag) {// 开始寻找结尾词
            off++;
            if (off >= terms.length) {
                endFreq = 10;
                flag = false;
            } else if (terms[off] != null) {
                int twoWordFreq = CONTEXT().ngramLibrary.getTwoWordFreq(term, terms[off]);
                if (twoWordFreq > 3) {
                    return null;
                }
                endFreq = terms[off].getTermNatures().personAttr.end + 1;
                flag = false;
            }
        }
        score -= Math.log(endFreq);
        score -= Math.log(beginFreq);

        if (score > -3 || (allFreq > 0 && undefinite > 0)) {
            return null;
        }

        skip = undefinite == 0;
        term = new Term(names.toString(), offe, TermNatures.NR);
        term.setSelfScore(score);
        return term;
    }

    public List<NewWord> getNewWords() {
        return _recogntion()
                .stream()
                .map(term -> new NewWord(term.getName(), AnsjContext.natureLibrary.NATURE_NR()))
                .collect(toList());
    }
}
