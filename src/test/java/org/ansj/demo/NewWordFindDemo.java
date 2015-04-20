package org.ansj.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.util.CollectionUtil;
import org.nlpcn.commons.lang.util.IOUtil;

/**
 * 这是一个新词发现的简单例子.
 * 
 * @author ansj
 * 
 */
public class NewWordFindDemo {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/三国演义.txt", "GBK");
		LearnTool learn = new LearnTool();
		NlpAnalysis nlpAnalysis = new NlpAnalysis(reader, learn);

		while (nlpAnalysis.next() != null) {
		}

		List<Entry<String, Double>> topTree = learn.getTopTree(0);

		StringBuilder sb = new StringBuilder();
		for (Entry<String, Double> entry : topTree) {
			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
		}

		IOUtil.Writer("/Users/ansj/Desktop/result.txt", IOUtil.UTF8, sb.toString());
	}
}