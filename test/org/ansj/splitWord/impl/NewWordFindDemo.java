package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.newWordFind.NewTerm;
import org.ansj.util.newWordFind.NewWordFind;

/**
 * 这是一个新词发现的简单例子.
 * 
 * @author ansj
 * 
 */
public class NewWordFindDemo {
	public static void main(String[] args) throws IOException {
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Downloads/《西游记》全集完整版TXT电子书.txt", "GBK");
		String content = null;
		Integer i = null;
		while ((content = reader.readLine()) != null) {
			TreeSet<NewTerm> newWords = NewWordFind.getNewWords(content);
			for (NewTerm newTerm : newWords) {
				if ((i = hm.get(newTerm.getName())) != null) {
					i++;
				} else {
					i = 1;
				}
				hm.put(newTerm.getName(), i) ;
			}
		}
		
		TreeSet<NewTerm> ts = new TreeSet<NewTerm>() ;
		Set<Entry<String, Integer>> entrySet = hm.entrySet() ;
		
		for (Entry<String, Integer> entry : entrySet) {
			Term term = new Term(entry.getKey(), 0, null) ;
			NewTerm newTerm = new NewTerm(term) ;
			newTerm.weight = entry.getValue() ;
			ts.add(newTerm) ;
		}
		int m = 0 ;
		for (NewTerm newTerm : ts) {
			m++ ;
			if(m==50){
				break ;
			}
			System.out.println(newTerm);
		}
	}
}