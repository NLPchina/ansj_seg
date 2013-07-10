package org.ansj.app.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class KeyWordComputer {

    private int nKeyword = 5;

    public KeyWordComputer() {
        nKeyword = 5;
    }

    /**
     * 返回关键词个数
     * @param nKeyword
     */
    public KeyWordComputer(int nKeyword) {
        this.nKeyword = nKeyword;

    }

    /**
     * 
     * @param content 正文
     * @return
     */
    private List<Keyword> computeArticleTfidf(String content, int titleLength) {
        Map<String, Keyword> tm = new HashMap<String, Keyword>();

        LearnTool learn = new LearnTool();
        List<Term> parse = NlpAnalysis.parse(content, learn);
        parse = NlpAnalysis.parse(content, learn);

        for (Term term : parse) {
            int weight = getWeight(term, content.length(), titleLength);
            if (weight == 0)
                continue;
            Keyword keyword = tm.get(term.getName());
            if (keyword == null) {
                keyword = new Keyword(term.getName(), term.getNatrue().allFrequency, weight);
                tm.put(term.getName(), keyword);
            } else {
                keyword.updateWeight(1);
            }
        }

        TreeSet<Keyword> treeSet = new TreeSet<Keyword>(tm.values());

        ArrayList<Keyword> arrayList = new ArrayList<Keyword>(treeSet);
        if (treeSet.size() < nKeyword) {
            return arrayList;
        } else {
            return arrayList.subList(0, nKeyword);
        }

    }

    /**
     * 
     * @param title 标题 
     * @param content   正文
     * @return
     */
    public Collection<Keyword> computeArticleTfidf(String title, String content) {
        return computeArticleTfidf(title + "\t" + content, title.length());
    }

    /**
     * 只有正文
     * @param content
     * @return
     */
    public Collection<Keyword> computeArticleTfidf(String content) {
        return computeArticleTfidf(content, 30);
    }

    private int getWeight(Term term, int length, int titleLength) {
        if (term.getName().matches("(?s)\\d.*")) {
            return 0;
        }

        if (term.getName().trim().length() < 2) {
            return 0;
        }

        String pos = term.getNatrue().natureStr;

        if (!pos.startsWith("n") || "num".equals(pos)) {
            return 0;
        }
        int weight = 0;

        if (titleLength > term.getOffe()) {
            return 20;
        }

        // position
        double position = (term.getOffe() + 0.0) / length;
        if (position < 0.05)
            return 10;
        weight += (5 - 5 * position);

        return weight;
    }

}
