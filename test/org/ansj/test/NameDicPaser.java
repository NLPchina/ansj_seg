package org.ansj.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class NameDicPaser {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = IOUtil.getReader(new FileInputStream("/Users/ansj/Documents/快盘/分词/library/name_temp.dic"), "UTF-8") ;
		String temp = null ;
		Analysis toAnalysis = null ;
		Term term = null ;
		int count =0 ;
		int all = 0 ;
		while((temp=reader.readLine())!=null){
			all ++ ;
			toAnalysis = new ToAnalysis(new StringReader(temp));
			while((term=toAnalysis.next())!=null){
				if(temp.length()==term.getName().length()){
					count ++ ;
				}
			}
		}
		
		System.out.println(count+"/"+all);
	}
}
