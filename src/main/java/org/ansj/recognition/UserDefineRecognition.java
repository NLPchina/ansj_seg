package org.ansj.recognition;

import org.ansj.Term;
import org.ansj.TermNature;
import org.ansj.TermNatures;
import org.nlpcn.commons.lang.tire.domain.Branch;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.WoodInterface;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static org.ansj.AnsjContext.CONTEXT;

/**
 * 用户自定义词典
 *
 * @author ansj
 */
public class UserDefineRecognition {

    private final Term[] terms;
    private final List<WoodInterface<String[], Branch>> forests;

    private WoodInterface<String[], Branch> forest;
    private WoodInterface<String[], Branch> branch;

    private int offe = -1;
    private int endOffe = -1;
    private int tempFreq = 50;
    private String tempNature;

    public UserDefineRecognition(final Term[] terms, final List<Forest> forests) {
        this.terms = terms;
        this.forests = forests != null && forests.size() > 0 ?
                unmodifiableList(forests) :
                singletonList(CONTEXT().getUserLibrary().getForest());
    }

    /**
     * 重置
     */
    private void reset() {
        this.branch = this.forest;
        this.offe = -1;
        this.endOffe = -1;
        this.tempFreq = 50;
        this.tempNature = null;
    }

    public void recognition() {
        for (final WoodInterface<String[], Branch> forest : this.forests) {
            if (forest == null) {
                continue;
            }
            reset();
            this.forest = forest;
            this.branch = this.forest;

            int length = terms.length - 1;

            for (int i = 0; i < length; i++) {
                if (this.terms[i] == null)
                    continue;

                final boolean flag = this.branch != forest;

                this.branch = termStatus(this.branch, this.terms[i]);
                if (this.branch == null) {
                    i = this.offe != -1 ? this.offe : i;
                    reset();
                } else if (this.branch.getStatus() == 3) {
                    this.endOffe = i;
                    this.tempNature = this.branch.getParam()[0];
                    this.tempFreq = getInt(this.branch.getParam()[1], 50);
                    if (this.offe != -1 && this.offe < this.endOffe) {
                        i = this.offe;
                        makeNewTerm();
                        reset();
                    } else {
                        reset();
                    }
                } else if (this.branch.getStatus() == 2) {
                    this.endOffe = i;
                    if (this.offe == -1) {
                        this.offe = i;
                    } else {
                        this.tempNature = this.branch.getParam()[0];
                        this.tempFreq = getInt(this.branch.getParam()[1], 50);
                        if (flag) {
                            makeNewTerm();
                        }
                    }
                } else if (this.branch.getStatus() == 1) {
                    this.offe = this.offe == -1 ? i : this.offe;
                }
            }
            if (this.offe != -1 && this.offe < this.endOffe) {
                makeNewTerm();
            }
        }
    }

    private int getInt(final String str, final int def) {
        try {
            return parseInt(str);
        } catch (final NumberFormatException e) {
            return def;
        }
    }

    private void makeNewTerm() {
        final StringBuilder sb = new StringBuilder();
        for (int j = this.offe; j <= this.endOffe; j++) {
            if (this.terms[j] != null) {
                sb.append(this.terms[j].getName());
            }
        }
        final TermNatures termNatures = new TermNatures(new TermNature(this.tempNature, this.tempFreq));
        final Term term = new Term(sb.toString(), this.offe, termNatures);
        term.setSelfScore(-1 * this.tempFreq);
        Term.insertTerm(this.terms, term);
    }

    /**
     * 传入一个term 返回这个term的状态
     *
     * @param branch branch
     * @param term   term
     * @return branch
     */
    private WoodInterface<String[], Branch> termStatus(final WoodInterface<String[], Branch> branch, final Term term) {
        WoodInterface<String[], Branch> b = branch;
        for (int j = 0; j < term.length(); j++) {
            b = b.getBranch(term.charAt(j));
            if (b == null) {
                return null;
            }
        }
        return b;
    }
}
