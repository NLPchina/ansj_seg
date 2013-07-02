package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;
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
			List<Term> parse = ToAnalysis.parse(mTemp) ;
			mTemp = listToString(parse).trim() ;
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
	
	
	public static String listToString(List<Term> parse){
		StringBuilder sb = new StringBuilder() ;
		for (Term term : parse) {
			sb.append(term.getName()) ;
			sb.append(" ") ;
		}
		return sb.toString() ;
		
	}
}
