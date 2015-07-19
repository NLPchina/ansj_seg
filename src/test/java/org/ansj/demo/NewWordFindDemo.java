package org.ansj.demo;

import org.ansj.splitWord.LearnTool;
import org.ansj.splitWord.NlpAnalysis;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static org.ansj.util.AnsjContext.NEW_LINE;
import static org.ansj.util.AnsjContext.TAB;

/**
 * 这是一个新词发现的简单例子.
 *
 * @author ansj
 */
public class NewWordFindDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/三国演义.txt", "GBK");
        LearnTool learn = new LearnTool();
        NlpAnalysis nlpAnalysis = new NlpAnalysis(new ArrayList<>(), learn, reader);

        while (nlpAnalysis.next() != null) {
        }

        List<Entry<String, Double>> topTree = learn.getTopTree(0);

        StringBuilder sb = new StringBuilder();
        for (Entry<String, Double> entry : topTree) {
            sb.append(entry.getKey() + TAB + entry.getValue() + NEW_LINE);
        }

        IOUtil.Writer("/Users/ansj/Desktop/result.txt", IOUtil.UTF8, sb.toString());
    }
}