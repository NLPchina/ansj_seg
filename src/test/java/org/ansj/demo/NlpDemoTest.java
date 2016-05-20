package org.ansj.demo;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

public class NlpDemoTest {
	public static void main(String[] args) throws IOException {
		Forest systemDefault = UserDefineLibrary.FOREST;
		
		NlpAnalysis nlp = new NlpAnalysis(new Forest[]{systemDefault});
		
		
		nlp.resetContent(new StringReader("2015年无锡市突发环境事件"));
		Term term = nlp.next();
		while(term!=null){
		
			System.out.println(term.getRealName() +"\t|\t"+term.getName());
			
			term = nlp.next();
		}
//		System.out.println(parse);
	}
	
	private static void insertWord(Forest forest,String keyword, String nature, int freq) {
		String[] paramers = new String[2];
		paramers[0] = nature;
		paramers[1] = String.valueOf(freq);
		Value value = new Value(keyword, paramers);
		Library.insertWord(forest, value);
	}
}
