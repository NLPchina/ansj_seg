package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.newWordFind.LearnTool;

public class NlpDemo {
	public static void main(String[] args) throws IOException {
		//学习机器是有状态的
		long start = System.currentTimeMillis() ;
		LearnTool learn = new LearnTool() ;
		BufferedReader materialsReader = IOUtil.getReader("/Users/ansj/Downloads/红楼梦.txt", "GBK");
		String temp = null ;
		while((temp=materialsReader.readLine())!=null){
			List<Term> paser = NlpAnalysis.paser(temp, learn) ;
//			System.out.println(paser);
		}
		
		System.out.println("这次训练已经学到了: "+learn.count+" 个词!");
		System.out.println(System.currentTimeMillis()-start);
		System.out.println(learn.getTopTree(100));
	}
}
