//package org.ansj.splitWord.impl;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//
//import org.ansj.domain.Term;
//import org.ansj.splitWord.analysis.FilterAnalysis;
//import org.ansj.splitWord.analysis.ToAnalysis;
//
//public class FilterAnalysisText {
//	public static void main(String[] args) throws IOException {
//		Reader reader = new InputStreamReader(new FileInputStream("/Users/ansj/Documents/快盘/冒死记录中国神秘事件（真全本）.txt"), "GBK");
//		FilterAnalysis toAnalysis = new FilterAnalysis(new ToAnalysis(reader));
//		Term next = null;
//		long start = System.currentTimeMillis();
//		StringBuilder sb = new StringBuilder();
//		int i = 0 ;
//		while ((next = toAnalysis.next()) != null) {
//			i++ ;
//			System.out.println(next.getName() + ":" + next.getMaxPath().getNatureStr());
////			sb.append(next.getName() + ":" + next.maxNature);
////			sb.append("\n");
//		}
//		System.out.println(i);
//		System.out.println(System.currentTimeMillis() - start);
////		IOUtil.Writer("/Users/ansj/Documents/快盘/冒死.txt", "UTF-8", sb.toString());
//
//	}
//}
