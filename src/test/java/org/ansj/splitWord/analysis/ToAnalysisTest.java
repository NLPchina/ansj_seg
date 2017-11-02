package org.ansj.splitWord.analysis;

import org.ansj.CorpusTest;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

public class ToAnalysisTest extends CorpusTest{

	@Test
	public void test() {
		MyStaticValue.isNameRecognition = false ;
		for (String string : lines) {
			System.out.println(ToAnalysis.parse(string));
		}
	}

}
