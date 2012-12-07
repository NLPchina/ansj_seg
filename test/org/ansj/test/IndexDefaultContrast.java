package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class IndexDefaultContrast {
	public static void main(String[] args) throws IOException {
		HashSet<String> hs = new HashSet<String>() ;
		BufferedReader br = IOUtil.getReader("library/stop/stopLibrary.dic", "UTF-8");
		
		String temp =null ;
		while((temp=br.readLine())!=null){
			temp = temp.trim().toLowerCase() ;
			hs.add(temp) ;
		}
		
		System.out.println(hs.contains("的"));
		
		
		br = IOUtil.getReader("data/1998年人民日报分词语料_未区分.txt", "GBK");
		Analysis ta = new ToAnalysis(br) ;
		Term term = null ;
		int count =0 ;
		int skip = 0 ;
		while((term=ta.next())!=null){
			count++ ;
		}
		System.out.println(count);
		br.close() ;
		br = IOUtil.getReader("data/1998年人民日报分词语料_未区分.txt", "GBK");
		ta = new IndexAnalysis(br) ;
		while((term=ta.next())!=null){
			if(!hs.contains(term.getName())){
				count++ ;
			}else{
				skip++ ;
			}
		}
		System.out.println(count);
		System.out.println(skip);
		br.close() ;
		
	}
}
