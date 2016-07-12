package org.ansj.splitWord.analysis;

import org.ansj.CorpusTest;
import org.ansj.library.UserDefineLibrary;
import org.junit.Test;

public class NlpAnalysisTest extends CorpusTest {

	@Test
	public void test() {
		for (String string : lines) {
//			System.out.println(NlpAnalysis.parse(string));
		}
		
		UserDefineLibrary.insertWord("沛县市", "ns", 2000);
		
		System.out.println(IndexAnalysis.parse("沛县市是一个很美丽的地方"));
	}

}
