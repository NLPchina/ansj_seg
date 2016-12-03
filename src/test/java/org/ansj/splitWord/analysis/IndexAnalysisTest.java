package org.ansj.splitWord.analysis;

import java.io.IOException;

import org.ansj.CorpusTest;
import org.junit.Test;

public class IndexAnalysisTest extends CorpusTest {

	@Test
	public void test() throws IOException {
		for (String string : lines) {
			System.out.println(IndexAnalysis.parse(string));
		}

	}

}
