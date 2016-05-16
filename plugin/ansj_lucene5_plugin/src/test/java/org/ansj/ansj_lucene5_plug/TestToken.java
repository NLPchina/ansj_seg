package org.ansj.ansj_lucene5_plug;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.ansj.library.UserDefineLibrary;
import org.ansj.lucene5.AnsjAnalyzer;
import org.ansj.lucene5.AnsjAnalyzer.TYPE;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TestToken {
	public static void main(String[] args) {
		
		UserDefineLibrary.insertWord("清华", "n", 2000);
		UserDefineLibrary.insertWord("大学", "n", 2000);
		
        Analyzer ca = new AnsjAnalyzer(TYPE.index);
        
        String content = "清华大学" ;
        
        try {
			TokenStream tokenStream = ca.tokenStream(content, new StringReader(content)) ;
			
			while(tokenStream.incrementToken()){
				CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class)  ;
				System.out.println(attribute);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ca.close();

	}
}
