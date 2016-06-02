package org.ansj.splitWord.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class NlpAnalysisTest {

	@Test
	public void test() {
		List<String> list = new ArrayList<String>();
		for (String string : list) {
			System.out.println(NlpAnalysis.parse(string));
		}
	}

}
