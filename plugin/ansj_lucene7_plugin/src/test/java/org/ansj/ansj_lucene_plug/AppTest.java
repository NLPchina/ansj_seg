package org.ansj.ansj_lucene_plug;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.*;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) throws IOException {

		String stopDicStr = "6\n7\n龙";

		StopRecognition testFilter = new StopRecognition();

		BufferedReader br = new BufferedReader(new StringReader(stopDicStr));
		String temp = null;
		while ((temp = br.readLine()) != null) {
			testFilter.insertStopWords(temp);
		}


		List<StopRecognition> filters = new ArrayList<StopRecognition>();
		filters.add(testFilter);
		
		for (int i = 0; i < 1; i++) {
			StringReader reader = new StringReader("龙虎胶囊 6 * 7cm");
			parse(new IndexAnalysis(reader), filters);
			parse(new ToAnalysis(reader), filters);
			parse(new DicAnalysis(reader), filters);
			parse(new NlpAnalysis(reader), filters);
			parse(new BaseAnalysis(reader), filters);
		}
	}

	private static void parse(Analysis an, List<StopRecognition> filters) throws IOException {
		Tokenizer tokenizer = new AnsjTokenizer(an, filters, null);
		while (tokenizer.incrementToken()) {
			CharTermAttribute attribute = tokenizer.getAttribute(CharTermAttribute.class);
			System.out.println(attribute);
		}
	}
}
