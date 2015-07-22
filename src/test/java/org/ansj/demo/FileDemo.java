package org.ansj.demo;

import org.ansj.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.BaseAnalysis;
import org.ansj.splitWord.ToAnalysis;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;

import static org.ansj.AnsjContext.TAB;

/**
 * 对文件进行分词的例子
 *
 * @author ansj
 */

public class FileDemo {
    public static void main(String[] args) throws IOException {
        //
        // MyStaticValue.realName = true;
        BufferedReader reader = IOUtil.getReader("/home/ansj/temp/360baikeData/360tag_all.txt", "utf-8");

        ToAnalysis.parse("test 123 孙");

        Analysis na = new BaseAnalysis(reader);

        long start = System.currentTimeMillis();
        int allCount = 0;
        Term term = null;
        while ((term = na.next()) != null) {
            if (term.getOffe() % 10000 == 0)
                System.out.println(term.getOffe() + TAB + term.getName());
            allCount += term.getName().length();

            if (allCount > 30000000) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("共 " + allCount + " 个字符，每秒处理了:" + (allCount * 1000.0 / (end - start)));
    }
}
