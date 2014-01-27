package org.ansj.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.MyStaticValue;

public class TestCRFAnalysis {
	public static void main(String[] args) throws IOException {

		BufferedReader reader = IOUtil.getReader("/home/ansj/文档/data/corpus/sport/sports.sohu.com.txt", "utf-8");

		String temp = null;
		MyStaticValue.getBigSplitWord() ;
		List<Term> parse = NlpAnalysis.parse("测试123孙建中国计算机研究有限公司123");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/ansj/文档/data/corpus/sport/sports.sohu.com.txt.result")));
		LearnTool learn = new LearnTool();
		int len = 0;
		long start = System.currentTimeMillis();
		int k = 0 ;
		while ((temp = reader.readLine()) != null) {
			if(k++>10000){
				break ;
			}
			len += temp.length();
			parse = NlpAnalysis.parse(temp, learn);
			bw.write(parse.toString());
			bw.write("\n");
		}
		System.out.println(len/(System.currentTimeMillis() - start)*1000);
		System.out.println(learn.getTopTree(100));
		bw.flush();
		bw.close();
	}
}
