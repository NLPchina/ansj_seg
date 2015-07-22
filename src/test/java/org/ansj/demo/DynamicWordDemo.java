package org.ansj.demo;

import org.ansj.Term;
import org.ansj.library.UserLibrary;
import org.ansj.splitWord.ToAnalysis;

import java.util.List;

import static org.ansj.AnsjContext.CONTEXT;

/**
 * 动态添加删除用户自定义词典!
 *
 * @author ansj
 */
public class DynamicWordDemo {

    public static void main(final String[] args) {
        final UserLibrary userLibrary = CONTEXT().getUserLibrary();

        // 增加新词,中间按照'\t'隔开
        userLibrary.insertWord("ansj中文分词", "userDefine", 1000);
        List<Term> terms = ToAnalysis.parse("我觉得Ansj中文分词是一个不错的系统!我是王婆!");
        System.out.println("增加新词例子:" + terms);

        // 删除词语,只能删除.用户自定义的词典.
        userLibrary.removeWord("ansj中文分词");
        terms = ToAnalysis.parse("我觉得ansj中文分词是一个不错的系统!我是王婆!");
        System.out.println("删除用户自定义词典例子:" + terms);
    }
}
