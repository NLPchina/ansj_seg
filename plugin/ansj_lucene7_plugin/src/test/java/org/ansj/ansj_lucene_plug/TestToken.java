package org.ansj.ansj_lucene_plug;

import org.ansj.library.DicLibrary;
import org.ansj.library.SynonymsLibrary;
import org.ansj.lucene7.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TestToken {
	public static void main(String[] args) {
		
		SynonymsLibrary.put(SynonymsLibrary.DEFAULT, "../../library/synonyms.dic");

		DicLibrary.insert(DicLibrary.DEFAULT, "清华", "n", 2000);
		DicLibrary.insert(DicLibrary.DEFAULT, "大学", "n", 2000);

		Map<String, String> map = new HashMap<String, String>();

		map.put("type", "dic_ansj");
		map.put(SynonymsLibrary.DEFAULT, SynonymsLibrary.DEFAULT);
		

		Analyzer ca = new AnsjAnalyzer(map);

		String content = "我爱北京天安门天安门上太阳升我美丽的清华大学";

		try {
			TokenStream tokenStream = ca.tokenStream(content, new StringReader(content));

			while (tokenStream.incrementToken()) {
				
				System.out.print(tokenStream.getAttribute(CharTermAttribute.class));
				System.out.print("\t") ;
				System.out.print(tokenStream.getAttribute(OffsetAttribute.class).startOffset());
				System.out.print("\t") ;
				System.out.print(tokenStream.getAttribute(PositionIncrementAttribute.class).getPositionIncrement());
				System.out.print("\t") ;
				System.out.println(tokenStream.getAttribute(TypeAttribute.class).type());
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ca.close();

	}
}
