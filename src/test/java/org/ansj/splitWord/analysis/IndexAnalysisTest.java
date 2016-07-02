package org.ansj.splitWord.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.ansj.CorpusTest;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import junit.framework.Assert;

public class IndexAnalysisTest extends CorpusTest {

	@Test
	public void test() throws IOException {
		for (String string : lines) {
			System.out.println(IndexAnalysis.parse(string));
		}

	}

}
