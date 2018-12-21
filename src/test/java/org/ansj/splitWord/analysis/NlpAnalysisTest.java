package org.ansj.splitWord.analysis;

import org.ansj.CorpusTest;
import org.junit.Test;

public class NlpAnalysisTest extends CorpusTest {

	@Test
	public void test() {
		System.out.println(NlpAnalysis.parse("崔永元炮轰范冰冰"));
		for (String string : lines) {
			System.out.println(NlpAnalysis.parse(string));
		}
	}

}
