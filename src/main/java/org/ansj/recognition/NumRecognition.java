package org.ansj.recognition;

import org.ansj.domain.Term;
import org.ansj.util.AnsjContext;

import static org.ansj.util.AnsjContext.CONTEXT;

public class NumRecognition {

    /**
     * 数字+数字合并,zheng
     *
     * @param terms
     */
    public static void recognition(final Term[] terms) {
        final AnsjContext context = CONTEXT();

        final int length = terms.length - 1;
        for (int i = 0; i < length; i++) {
            if (terms[i] == null) {
                continue;
            } else if (".".equals(terms[i].getName()) || "．".equals(terms[i].getName())) {
                // 如果是.前后都为数字进行特殊处理
                Term to = terms[i].getTo();
                Term from = terms[i].getFrom();
                if (from.getTermNatures().numAttr.flag && to.getTermNatures().numAttr.flag) {
                    from.setName(from.getName() + "." + to.getName());
                    Term.termLink(from, to.getTo());
                    terms[to.getOffe()] = null;
                    terms[i] = null;
                    i = from.getOffe() - 1;
                }
                continue;
            } else if (!terms[i].getTermNatures().numAttr.flag) {
                continue;
            }

            Term temp = terms[i];
            // 将所有的数字合并
            while ((temp = temp.getTo()).getTermNatures().numAttr.flag) {
                terms[i].setName(terms[i].getName() + temp.getName());
            }
            // 如果是数字结尾
            if (context.isQuantifierRecognition && temp.getTermNatures().numAttr.numEndFreq > 0) {
                terms[i].setName(terms[i].getName() + temp.getName());
                temp = temp.getTo();
            }

            // 如果不等,说明terms[i]发生了改变
            if (terms[i].getTo() != temp) {
                Term.termLink(terms[i], temp);
                // 将中间无用元素设置为null
                for (int j = i + 1; j < temp.getOffe(); j++) {
                    terms[j] = null;
                }
                i = temp.getOffe() - 1;
            }
        }
    }
}
