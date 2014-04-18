package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.FilterModifWord;

public class StopWordDemo {
	public static void main(String[] args) {
        FilterModifWord.insertStopWord("五一");
        List<Term> parseResultList = NlpAnalysis.parse("五一，劳动节快乐");
        parseResultList = FilterModifWord.modifResult(parseResultList);
        System.out.println(parseResultList);
	}
}
