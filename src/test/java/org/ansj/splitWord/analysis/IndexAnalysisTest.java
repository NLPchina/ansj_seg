package org.ansj.splitWord.analysis;

import org.ansj.CorpusTest;
import org.junit.Test;

import java.io.IOException;

public class IndexAnalysisTest extends CorpusTest {

	@Test
	public void test() throws IOException {
		for (String string : lines) {
			System.out.println(IndexAnalysis.parse(string));
		}

	}

}
