package org.ansj.demo;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.ToAnalysis;
import org.ansj.util.FilterModifWord;

import java.util.List;

import static org.ansj.util.AnsjContext.CONTEXT;

public class UserDefineLibraryNatureDemo {

    public static void main(final String[] args) {
        final UserDefineLibrary userDefineLibrary = CONTEXT().getUserDefineLibrary();
        final FilterModifWord filterModifWord = new FilterModifWord();

        //增加词汇
        userDefineLibrary.insertWord("ansj大神", "作者", 1000);
        userDefineLibrary.insertWord("eye.kuyun.com", "网站", 1000);

        List<Term> parse = ToAnalysis.parse("Ansj大神是eye.kuyun.com网站的开发者");

        System.out.println(parse);

        //自定义词性优先
        parse = filterModifWord.modifResult(parse);

        System.out.println(parse);
    }
}
