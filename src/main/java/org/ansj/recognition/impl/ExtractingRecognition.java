package org.ansj.recognition.impl;

import org.ansj.app.extracting.Extracting;
import org.ansj.app.extracting.domain.ExtractingResult;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.Recognition;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于规则引擎的结果识别
 * Created by XYUU <xyuu@xyuu.net> on 2018/7/14.
 */
public class ExtractingRecognition implements Recognition {

	private static final long serialVersionUID = 1L;
	
	private Extracting extracting;
    private TermNatures termNatures;

    public ExtractingRecognition(Extracting extracting, TermNatures termNatures) {
        this.extracting = extracting;
        this.termNatures = termNatures;
    }

    @Override
    public void recognition(Result result) {
        ExtractingResult parse = extracting.parse(result);
        LinkedList<int[]> extracted = new LinkedList<>();
        int len = 0;
        int[] cur = new int[]{0, 0};
        for (List<Term> list : parse.findAll()) {
            String name = list.get(list.size() - 1).getName();
            while ("-".equals(name) || ".".equals(name)) {
                list.remove(list.size() - 1);
                name = list.get(list.size() - 1).getName();
            }
            if (list.size() == 1) {
                continue;
            }
            int beginOff = list.get(0).getOffe();
            int endOff = list.get(list.size() - 1).getOffe() + list.get(list.size() - 1).getName().length();
            if (beginOff >= cur[1]) {
                extracted.add(cur = new int[]{beginOff, endOff});
            } else if (endOff - beginOff > cur[1] - cur[0]) {
                extracted.removeLast();
                extracted.add(cur = new int[]{beginOff, endOff});
            }
            len += list.size() - 2;
        }
        List<Term> terms = result.getTerms();
        List<Term> newList = new ArrayList<>();
        cur = extracted.poll();
        StringBuilder sb = new StringBuilder();
        StringBuilder sbReal = new StringBuilder();
        for (Term term : terms) {
            if (cur == null) {
                newList.add(term);
                continue;
            }
            if (term.getOffe() < cur[0]) {
                newList.add(term);
            } else if (term.getOffe() >= cur[1]) {
                if (sb.length() > 0) {
                    Term newTerm = new Term(sb.toString(), cur[0], termNatures);
                    if (sbReal.length() > 0) {
                        newTerm.setRealName(sbReal.toString());
                    }
                    newList.add(newTerm);
                    sb.delete(0, sb.length());
                    sbReal.delete(0, sbReal.length());
                } else {
                    LogFactory.getLog(this.getClass()).warn(
                            "Empty name while extracting! " + result.toStringWithOutNature(""));
                }
                cur = extracted.poll();
                newList.add(term);
            } else {
                sb.append(term.getName());
                if (term.getRealNameIfnull() != null) {
                    sbReal.append(term.getRealName());
                }
            }
        }
        if (cur != null && sb.length() > 0) {
            Term newTerm = new Term(sb.toString(), cur[0], termNatures);
            if (sbReal.length() > 0) {
                newTerm.setRealName(sbReal.toString());
            }
            newList.add(newTerm);
        }
        result.setTerms(newList);
    }
}
