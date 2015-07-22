package org.ansj.recognition;

import org.ansj.*;
import org.ansj.library.NatureLibrary;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

import static org.ansj.AnsjContext.CONTEXT;

/**
 * 新词识别
 *
 * @author ansj
 */
public class NewWordRecognition {

    private final Term[] terms;

    private final SmartForest<NewWord> forest;

    private Term from;

    private Term to;

    private SmartForest<NewWord> branch;

    // 偏移量
    private int offe;

    private double score;

    private Nature tempNature;

    private StringBuilder sb = new StringBuilder();

    public NewWordRecognition(final Term[] terms, final SmartForest<NewWord> forest) {
        this.terms = terms;
        this.forest = forest;
        this.branch = this.forest;
    }

    /**
     * 重置
     */
    private void reset() {
        this.branch = this.forest;
        this.offe = -1;
        this.tempNature = null;
        this.score = 0;
        this.sb = new StringBuilder();
    }

    public void recognition() {
        if (branch == null) {
            return;
        }

        for (int i = 0; i < terms.length - 1; i++) {
            final Term term = terms[i];
            if (term == null) {
                continue;
            }

            from = term.getFrom();
            term.setScore(0);
            term.setSelfScore(0);
            branch = branch.getBranch(term.getName());

            if (branch == null || branch.getStatus() == 3) {
                reset();
                continue;
            }

            offe = i;

            // 循环查找添加
            sb.append(term.getName());
            if (branch.getStatus() == 2) {
                term.setSelfScore(branch.getParam().getScore());
            }
            Term t = term;
            boolean flag = true;
            while (flag) {
                t = t.getTo();
                branch = branch.getBranch(t.getName());
                // 如果没有找到跳出
                if (branch == null) {
                    break;
                }

                switch (branch.getStatus()) {
                    case 1:
                        sb.append(t.getName());
                        continue;
                    case 2:
                        sb.append(t.getName());
                        score = branch.getParam().getScore();
                        tempNature = branch.getParam().getNature();
                        to = t.getTo();
                        makeNewTerm();
                        continue;
                    case 3:
                        sb.append(t.getName());
                        score = branch.getParam().getScore();
                        tempNature = branch.getParam().getNature();
                        to = t.getTo();
                        makeNewTerm();
                        flag = false;
                        break;
                    default:
                        System.out.println("怎么能出现0呢?");
                        break;
                }
            }
            reset();
        }
    }

    private void makeNewTerm() {
        final Term term = new Term(sb.toString(), offe, new TermNatures(new TermNature(tempNature.natureStr, 1)));
        term.setSelfScore(score);
        term.setNature(tempNature);
        if (sb.length() > 3) {
            term.setSubTerm(Term.subTerms(from, to));
        }
        Term.termLink(from, term);
        Term.termLink(term, to);
        Term.insertTerm(terms, term);
        parseNature(term);
    }

    /**
     * 得到细颗粒度的分词，并且确定词性
     * <p>
     * 返回是null说明已经是最细颗粒度
     */
    static void parseNature(final Term term) {
        final NatureLibrary natureLibrary = AnsjContext.natureLibrary;

        if (!natureLibrary.NATURE_NW().equals(term.getNature()) || term.getName().length() <= 3) {
            return;
        }
        if (ForeignPersonRecognition.isFName(term.getName())) {// 是否是外国人名
            term.setNature(natureLibrary.getNature("nrf"));
            return;
        }

        // 判断是否是机构名
        final Term first = term.getSubTerm().get(0);
        final Term last = term.getSubTerm().get(term.getSubTerm().size() - 1);
        final int[] is = CONTEXT().companyAttrLibrary.getCompanyMap().get(last.getName());
        int all = 0;
        if (is != null) {
            all += is[1];
        }
        if (all > 1000) {
            term.setNature(natureLibrary.getNature("nt"));
        }
    }
}
