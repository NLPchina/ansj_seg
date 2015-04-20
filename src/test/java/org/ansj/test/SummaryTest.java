package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;

import org.ansj.app.summary.SummaryComputer;
import org.ansj.app.summary.TagContent;
import org.ansj.app.summary.pojo.Summary;
import org.nlpcn.commons.lang.util.IOUtil;

public class SummaryTest {
	public static void main(String[] args) throws IOException {

		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/temp/test.txt", "utf-8");

		String content = null;

		TagContent tw = new TagContent("<begin>", "<end>");

		while ((content = reader.readLine()) != null) {
			String title = null;

			SummaryComputer summaryComputer = new SummaryComputer(300, title, content);

			Summary summary = summaryComputer.toSummary();

			System.out.println(summary.getKeyWords()); // 关键词

			String tagContent = tw.tagContent(summary); // 标记后的摘要

			System.out.println(tagContent);
		}
	}
}
