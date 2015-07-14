package org.ansj.recognition;

import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.library.UserDefineLibrary;
import org.ansj.util.TermUtil;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.WoodInterface;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

/**
 * 用户自定义词典. 又称补充词典
 *
 * @author ansj
 */
public class UserDefineRecognition {

    private final Term[] terms;
    private final List<? extends WoodInterface> forests;

    private int offe = -1;
    private int endOffe = -1;
    private int tempFreq = 50;
    private String tempNature;

    private WoodInterface branch = null;
    private WoodInterface forest = null;

    public UserDefineRecognition(final Term[] terms, final List<Forest> forests) {
        this.terms = terms;
        this.forests = forests != null && forests.size() > 0 ?
                unmodifiableList(forests) :
                singletonList(UserDefineLibrary.getInstance().getForest());
    }

    public void recognition() {
        for (final WoodInterface forest : forests) {
            if (forest == null) {
                continue;
            }
            reset();
            this.forest = forest;
            this.branch = forest;

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
                    this.tempNature = this.branch.getParams()[0];
                    this.tempFreq = getInt(this.branch.getParams()[1], 50);
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
                        this.tempNature = this.branch.getParams()[0];
                        this.tempFreq = getInt(this.branch.getParams()[1], 50);
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
            return Integer.parseInt(str);
        } catch (final NumberFormatException e) {
            return def;
        }
    }

    private void makeNewTerm() {
        StringBuilder sb = new StringBuilder();
        for (int j = this.offe; j <= this.endOffe; j++) {
            if (this.terms[j] != null) {
                sb.append(this.terms[j].getName());
            }
            // terms[j] = null;
        }
        final TermNatures termNatures = new TermNatures(new TermNature(this.tempNature, this.tempFreq));
        final Term term = new Term(sb.toString(), this.offe, termNatures);
        term.selfScore(-1 * this.tempFreq);
        TermUtil.insertTerm(this.terms, term);
        // reset();
    }

    /**
     * 重置
     */
    private void reset() {
        this.offe = -1;
        this.endOffe = -1;
        this.tempFreq = 50;
        this.tempNature = null;
        this.branch = this.forest;
    }

    /**
     * 传入一个term 返回这个term的状态
     *
     * @param branch branch
     * @param term   term
     * @return branch
     */
    private WoodInterface termStatus(WoodInterface branch, final Term term) {
        final String name = term.getName();
        for (int j = 0; j < name.length(); j++) {
            branch = branch.get(name.charAt(j));
            if (branch == null) {
                return null;
            }
        }
        return branch;
    }

}
