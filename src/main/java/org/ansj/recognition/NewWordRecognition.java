package org.ansj.recognition;

import org.ansj.domain.*;
import org.ansj.library.NatureLibrary;
import org.ansj.splitWord.LearnTool;
import org.ansj.util.AnsjContext;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

import static org.ansj.util.AnsjContext.CONTEXT;

/**
 * 新词识别
 *
 * @author ansj
 */
public class NewWordRecognition {

    private Term[] terms = null;

    private double score;

    private StringBuilder sb = new StringBuilder();

    private SmartForest<NewWord> forest = null;

    private SmartForest<NewWord> branch = null;

    // private int offe = -1;
    // private int endOffe = -1;
    private Nature tempNature;

    private Term from;

    private Term to;

    // 偏移量
    private int offe;

    public NewWordRecognition(Term[] terms, LearnTool learn) {
        this.terms = terms;
        forest = learn.getForest();
        branch = learn.getForest();
    }

    public void recognition() {
        if (branch == null) {
            return;
        }
        int length = terms.length - 1;

        Term term = null;
        for (int i = 0; i < length; i++) {
            if (terms[i] == null) {
                continue;
            } else {
                from = terms[i].getFrom();
                terms[i].setScore(0);
                terms[i].setSelfScore(0);
            }

            branch = branch.getBranch(terms[i].getName());

            if (branch == null || branch.getStatus() == 3) {
                reset();
                continue;
            }

            offe = i;

            // 循环查找添加
            term = terms[i];
            sb.append(term.getName());
            if (branch.getStatus() == 2) {
                term.setSelfScore(branch.getParam().getScore());
            }
            boolean flag = true;
            while (flag) {
                term = term.getTo();
                branch = branch.getBranch(term.getName());
                // 如果没有找到跳出
                if (branch == null) {
                    break;
                }

                switch (branch.getStatus()) {
                    case 1:
                        sb.append(term.getName());
                        continue;
                    case 2:
                        sb.append(term.getName());
                        score = branch.getParam().getScore();
                        tempNature = branch.getParam().getNature();
                        to = term.getTo();
                        makeNewTerm();
                        continue;
                    case 3:
                        sb.append(term.getName());
                        score = branch.getParam().getScore();
                        tempNature = branch.getParam().getNature();
                        to = term.getTo();
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
        Term term = new Term(sb.toString(), offe, new TermNatures(new TermNature(tempNature.natureStr, 1)));
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
     * 重置
     */
    private void reset() {
        offe = -1;
        tempNature = null;
        branch = forest;
        score = 0;
        sb = new StringBuilder();
    }


    /**
     * 得到细颗粒度的分词，并且确定词性
     * <p>
     * 返回是null说明已经是最细颗粒度
     */
    static void parseNature(final Term term) {
        final NatureLibrary natureLibrary = AnsjContext.natureLibrary;

        if (!natureLibrary.NATURE_NW().equals(term.getNature())) {
            return;
        }
        if (term.getName().length() <= 3) {
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
