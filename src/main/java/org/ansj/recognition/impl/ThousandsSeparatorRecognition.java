package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;

import java.util.*;

/**
 * DESC: 千分位格式数字识别（如：1,234,567.11、1,234,567）
 *
 * @author baicaixiaozhan
 * @since v5.1.6
 */
public class ThousandsSeparatorRecognition implements Recognition {

    private static final TermNatures THOUSANDS_SEPARATOR_M = new TermNatures(new TermNature("thousands_separator", 1));
    private String separator;

    public ThousandsSeparatorRecognition() {
        this.separator = ",";
    }

    public ThousandsSeparatorRecognition(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public void recognition(Result result) {
        List<Term> terms = result.getTerms();
        if (terms.isEmpty()) {
            return;
        }

        for (Term term : terms) {
            if (term.getOffe() == -1) {
                continue;
            }

            if (Objects.equals(term.termNatures(), TermNatures.M_ALB) && isMatchThousands(term.to())) {
                // 处理千分位格式数字
                doMerge(term);
                term.updateTermNaturesAndNature(THOUSANDS_SEPARATOR_M);

                Term to = term.to();
                while (isMatchThousands(to)) {
                    doMerge(term);
                    to = term.to();
                }
            }
        }

        for (Iterator<Term> iterator = terms.iterator(); iterator.hasNext();) {
            Term term = iterator.next();
            if (term.getOffe() == -1) {
                iterator.remove();
            }
        }
    }

    private void doMerge(Term term) {
        Term to1 = term.to();
        term.merage(to1);
        to1.setOffe(-1);

        Term to2 = term.to();
        term.merage(to2);
        to2.setOffe(-1);
    }

    private boolean isMatchThousands(Term term) {
        return Objects.equals(term.getName(), separator)
                && (
                (term.from().getName().contains(separator) && term.from().getName().indexOf(separator) <= 3)
                || (!term.from().getName().contains(separator) && term.from().getName().length() <= 3)
                )
                && Objects.equals(term.to().termNatures(), TermNatures.M_ALB)
                && ((term.to().getName().contains(".") && term.to().getName().indexOf(".") == 3)
                || (!term.to().getName().contains(".") && term.to().getName().length() == 3));
    }

}
