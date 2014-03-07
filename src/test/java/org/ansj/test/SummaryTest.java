package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import love.cq.util.IOUtil;

import org.ansj.app.summary.SummaryComputer;
import org.ansj.app.summary.TagContent;
import org.ansj.app.summary.pojo.Summary;

public class SummaryTest {
	public static void main(String[] args) throws IOException {
		
		String content = IOUtil.getContent("//home/ansj/文档/data/corpus/sport/足球/1soccer.sports.sohu.com.txt","utf-8") ;
		
		BufferedReader br = new BufferedReader(new StringReader(content)) ;


		TagContent tw = new TagContent("<begin>", "<end>");
		
		while((content=br.readLine())!=null){
			String title = null;

			SummaryComputer summaryComputer = new SummaryComputer(300, title, content);

			Summary summary = summaryComputer.toSummary();
System.out.println(content);
			System.out.println(summary.getKeyWords()); // 关键词

			String tagContent = tw.tagContent(summary); // 标记后的摘要

			System.out.println(tagContent);
		}
	}
}
