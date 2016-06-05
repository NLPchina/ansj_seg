package org.ansj.splitWord.analysis;

import org.ansj.CorpusTest;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

public class NlpAnalysisTest extends CorpusTest {

	@Test
	public void test() {
//		MyStaticValue.CRF.put(MyStaticValue.CRF_DEFAULT, "/Users/sunjian/Documents/src/CRF++-0.58/test/model.txt") ; 
		
		for (String string : lines) {
			System.out.println(NlpAnalysis.parse(string));
		}
	}

}
