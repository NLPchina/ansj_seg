package org.ansj.test;
//package org.ansj.splitWord.impl;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.TreeSet;
//
//import love.cq.util.IOUtil;
//
//import org.ansj.domain.Term;
//import org.ansj.splitWord.analysis.ToAnalysis;
//import org.ansj.util.newWordFind.NewTerm;
//import org.ansj.util.newWordFind.NewWordFind;
//
//public class CSDNBLogTest {
//	public static void main(String[] args) throws IOException {
//		HashSet<String> hs = new HashSet<String>();
//		BufferedReader filter = IOUtil.getReader("library/stop/stopLibrary.dic", "UTF-8");
//		String temp = null;
//		while ((temp = filter.readLine()) != null) {
//			temp = temp.trim().toLowerCase();
//			hs.add(temp);
//		}
//		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/temp/blogBigFile.txt", "UTF-8");
//		String content = null;
//		ToAnalysis.parse("init");
//		long start = System.currentTimeMillis();
//		int size = 0;
//		int resultSize = 0;
//		long length = 0;
//		int i = 0;
//		List<Term> all = null;
//		HashMap<String, Integer> hm = new HashMap<String, Integer>();
//		Integer num = null;
//		while ((content = reader.readLine()) != null) {
//			i++;
//			length += content.length();
//			// all = ToAnalysis.parse(content);
//			// size += all.size();
//			TreeSet<NewTerm> newWords = new NewWordFind().getNewWords(content);
//
//			for (NewTerm newTerm : newWords) {
//				if ((num = hm.get(newTerm.getName())) != null) {
//					hm.put(newTerm.getName(), ++num);
//				} else {
//					hm.put(newTerm.getName(), 1);
//				}
//			}
//
//			if (i % 10000 == 0) {
//				break;
//			}
//		}
//
//		Set<Entry<String, Integer>> entrySet = hm.entrySet();
//		StringBuilder sb = new StringBuilder();
//		for (Entry<String, Integer> entry : entrySet) {
//			sb.append(entry.getKey()+"\t"+entry.getValue()) ;
//			sb.append("\n") ;
//		}
//		IOUtil.Writer("/Users/ansj/Documents/temp/newWord.txt", "UTF-8", sb.toString()); 
//
//		System.out.println(size + "\t" + resultSize);
//		System.out.println((length * (long) 1000) / (System.currentTimeMillis() - start));
//		System.out.println(System.currentTimeMillis() - start);
//
//	}
//
//	public static void parse() {
//
//	}
//}
