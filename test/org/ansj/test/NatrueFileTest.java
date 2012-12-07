package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

public class NatrueFileTest {
	public static void main(String[] args) throws IOException {
		StringBuilder sb = new StringBuilder() ;
		String temp = null ;
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/社交焦虑的治疗方式.txt", "GBK") ;
		while((temp=reader.readLine())!=null){
			sb.append(temp) ;
		}
		
		List<Term> paser = ToAnalysis.paser(sb.toString()) ;
		
		new NatureRecognition(paser).recognition() ;
		
		
		System.out.println(paser);

	}
}
