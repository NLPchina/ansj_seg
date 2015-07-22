package org.ansj.demo;

import org.ansj.Term;
import org.ansj.library.UserLibrary;
import org.ansj.splitWord.ToAnalysis;
import org.ansj.splitWord.FilterModifWord;

import java.util.List;

import static org.ansj.AnsjContext.CONTEXT;

public class UserDefineLibraryNatureDemo {

    public static void main(final String[] args) {
        final UserLibrary userLibrary = CONTEXT().getUserLibrary();
        final FilterModifWord filterModifWord = new FilterModifWord();

        //增加词汇
        userLibrary.insertWord("ansj大神", "作者", 1000);
        userLibrary.insertWord("eye.kuyun.com", "网站", 1000);

        List<Term> parse = ToAnalysis.parse("Ansj大神是eye.kuyun.com网站的开发者");

        System.out.println(parse);

        //自定义词性优先
        parse = filterModifWord.modifResult(parse);

        System.out.println(parse);
    }
}
