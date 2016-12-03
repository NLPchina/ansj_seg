package org.ansj.app.crf;

import org.ansj.CorpusTest;
import org.ansj.library.CrfLibrary;
import org.junit.Test;

public class SplitWordTest extends CorpusTest {

	@Test
	public void test() {
		SplitWord crfSplitWord = CrfLibrary.get();
		for (String string : lines) {
			System.out.println(crfSplitWord.cut(string));
		}
	}

}
