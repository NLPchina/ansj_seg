package org.ansj.demo;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.ToAnalysis;

import java.util.List;

/**
 * 动态添加删除用户自定义词典!
 *
 * @author ansj
 */
public class DynamicWordDemo {

    public static void main(final String[] args) {
        final UserDefineLibrary userDefineLibrary = UserDefineLibrary.getInstance();

        // 增加新词,中间按照'\t'隔开
        userDefineLibrary.insertWord("ansj中文分词", "userDefine", 1000);
        List<Term> terms = ToAnalysis.parse("我觉得Ansj中文分词是一个不错的系统!我是王婆!");
        System.out.println("增加新词例子:" + terms);

        // 删除词语,只能删除.用户自定义的词典.
        userDefineLibrary.removeWord("ansj中文分词");
        terms = ToAnalysis.parse("我觉得ansj中文分词是一个不错的系统!我是王婆!");
        System.out.println("删除用户自定义词典例子:" + terms);
    }
}
