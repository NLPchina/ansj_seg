package org.ansj.demo;

import org.ansj.Term;
import org.ansj.library.UserLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.ToAnalysis;
import org.ansj.splitWord.FilterModifWord;

import java.util.List;

import static org.ansj.AnsjContext.CONTEXT;

public class FilterAndUpdateNatureDemo {

    public static void main(final String[] args) {
        final UserLibrary userLibrary = CONTEXT().getUserLibrary();

        // 加入停用词, 过滤词性词性
        final FilterModifWord filterModifWord = new FilterModifWord()
                .withStopWords("并且", "但是")
                .withStopNatures("w", "m", "null");


        List<Term> parse = ToAnalysis.parse("停用词过滤了.并且修正词143922950性为用户自定义词性.但是你必须.must.设置停用词性词性词典");
        new NatureRecognition(parse).recognition();
        System.out.println(parse);

        userLibrary.insertWord("停用词", "userDefine", 1000);

        // 修正词性并且过滤停用
        parse = filterModifWord.modifResult(parse);

        System.out.println(parse);
    }
}
