package org.ansj.demo;

import java.io.BufferedReader;
import java.io.IOException;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 对文件进行分词的例子
 * 
 * @author ansj
 * 
 */

public class FileDemo {
	public static void main(String[] args) throws IOException {
//
//		MyStaticValue.isRealName = true;
		BufferedReader reader = IOUtil.getReader("/home/ansj/公共的/corpus/result.txt", "utf-8");
		NlpAnalysis.parse("test 123 孙");
		
		Analysis na = new ToAnalysis(reader) ;
		
		long start = System.currentTimeMillis();
		int allCount = 0;
		Term term = null ;
		while((term=na.next())!=null){
			allCount += term.getName().length() ;
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println("共 " + allCount + " 个字符，每秒处理了:" + (allCount * 1000.0 / (end - start)));
	}
}
