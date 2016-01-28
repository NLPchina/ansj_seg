package org.ansj.lucene3;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class TestFilter {
	public static void main(String[] args) throws IOException {
		HashSet<String> filter = new HashSet<String>() ;
		
		filter.add("丢") ;
		filter.add("了") ;
		
		AnsjAnalysis analyzer = new AnsjAnalysis(filter,false);

		TokenStream ts = analyzer.tokenStream("hello", new StringReader("丢了"));
		Token token;
		try {
			while (ts.incrementToken()) {
				System.out.println(ts.getAttribute(TermAttribute.class).term());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
}
