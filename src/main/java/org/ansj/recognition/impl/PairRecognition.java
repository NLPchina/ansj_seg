package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 成对出现的，如书名号、单双引号、大小括号等等
 * Created by XYUU <xyuu@xyuu.net> on 2018/7/14.
 */
public class PairRecognition implements Recognition {

	private static final long serialVersionUID = 1L;

	private Nature nature;

    private Map<String, String> ruleMap;

    public PairRecognition(String natureStr, Map<String, String> ruleMap) {
        this.nature = new Nature(natureStr);
        this.ruleMap = ruleMap;
    }

    @Override
    public void recognition(Result result) {
        List<Term> terms = result.getTerms();
        String end = null;
        String name;

        LinkedList<Term> mergeList = null;

        List<Term> list = new LinkedList<Term>();

        for (Term term : terms) {
            name = term.getName();
            if (end == null) {
                if ((end = ruleMap.get(name)) != null) {
                    mergeList = new LinkedList<Term>();
                    mergeList.add(term);
                } else {
                    list.add(term);
                }
            } else {
                mergeList.add(term);
                if (end.equals(name)) {

                    Term ft = mergeList.pollFirst();
                    for (Term sub : mergeList) {
                        ft.merage(sub);
                    }
                    ft.setNature(nature);
                    list.add(ft);
                    mergeList = null;
                    end = null;
                }
            }
        }

        if (mergeList != null) {
            list.addAll(mergeList);
        }
        result.setTerms(list);
    }
}
