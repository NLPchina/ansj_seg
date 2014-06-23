package org.ansj.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.splitWord.impl.GetWordsImpl;
import org.junit.Test;
import org.nlpcn.commons.lang.util.IOUtil;

public class SpeedTest {
	public static void main(String[] args) throws IOException {
		ToAnalysis.parse("test---aaaa中国孙健测试");
		BufferedReader reader = IOUtil.getReader("/home/ansj/data/allSportsArticle", IOUtil.UTF8);

		long start = System.currentTimeMillis();
		long allCount = 0;
//		for (int j = 0; j < 1; j++) {
//			for (String string : all) {
//				allCount += string.length();
//				ToAnalysis.parse(string);
//			}
//		}
		
//		String temp = null ;
//		while((temp=reader.readLine())!=null){
//			GetWordsImpl gwi = new GetWordsImpl(temp) ;
//			allCount += temp.length() ;
//			while((gwi.allWords())!=null){
//				
//			}
//		}
		
		
		
		ToAnalysis toAnalysis = new ToAnalysis(IOUtil.getReader("/home/ansj/data/allSportsArticle", IOUtil.UTF8)); 
		
		Term term = null ;
		while((term = toAnalysis.next())!=null){
			allCount += term.getName().length();
		}

		long end = System.currentTimeMillis();
		System.out.println(start - end);
		System.out.println("共 " + allCount + " 个字符，每秒处理了:" + (allCount * 1000 / (end - start)));
	}
}
