package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import love.cq.util.IOUtil;

public class AccuracyTest {
	public static void main(String[] args) throws IOException {

		BufferedReader materialsReader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/语料/1998年人民日报分词语料_未区分.txt", "GBK");
		BufferedReader resultReader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/语料/1998年人民日报分词语料.txt", "GBK");
		String mTemp = null;
		String rTemp = null;
		int z =0 ;
		int b =0 ;
		while((mTemp=materialsReader.readLine())!=null&&(rTemp=resultReader.readLine())!=null){
			List<Term> paser = ToAnalysis.paser(mTemp) ;
			mTemp = listToString(paser).trim() ;
			rTemp = rTemp.trim() ;
			if(mTemp.equals(rTemp)){
				z++ ;
//				System.out.println("m:" + mTemp);
//				System.out.println("r:"+rTemp);
			}else{
				b++ ;
				System.out.println("m:" + mTemp);
				System.out.println("r:"+rTemp);
			}
		}
		
		System.out.println(z+"/"+(z+b)+"="+((double)z/(z+b)));
		
		materialsReader.close() ;
		resultReader.close() ;

	}
	
	
	public static String listToString(List<Term> paser){
		StringBuilder sb = new StringBuilder() ;
		for (Term term : paser) {
			sb.append(term.getName()) ;
			sb.append(" ") ;
		}
		return sb.toString() ;
		
	}
}
