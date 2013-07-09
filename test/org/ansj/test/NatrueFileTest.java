package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

public class NatrueFileTest {
	public static void main(String[] args) throws IOException {
		StringBuilder sb = new StringBuilder() ;
		String temp = null ;
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/社交焦虑的治疗方式.txt", "GBK") ;
		while((temp=reader.readLine())!=null){
			List<Term> parse = BaseAnalysis.parse(temp) ;
			for (Term term : parse) {
				sb.append(term.getName()) ;
				sb.append("\t") ;
			}
			sb.append("\n") ;
			
		}
		
		IOUtil.Writer("/Users/ansj/Desktop/result.txt", IOUtil.UTF8, sb.toString()); 
		
		
		
		

	}
}
