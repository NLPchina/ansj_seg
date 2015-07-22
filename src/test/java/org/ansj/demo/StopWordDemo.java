package org.ansj.demo;

import org.ansj.Term;
import org.ansj.splitWord.NlpAnalysis;
import org.ansj.splitWord.FilterModifWord;

import java.util.List;

public class StopWordDemo {

    public static void main(String[] args) {
        final FilterModifWord filterModifWord = new FilterModifWord()
                .withStopWords("五一");
        List<Term> parseResultList = NlpAnalysis.nlpParse("五一，劳动节快乐");
        parseResultList = filterModifWord.modifResult(parseResultList);
        System.out.println(parseResultList);
    }
}
