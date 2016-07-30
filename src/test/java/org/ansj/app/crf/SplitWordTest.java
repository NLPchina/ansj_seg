package org.ansj.app.crf;

import org.ansj.CorpusTest;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

public class SplitWordTest extends CorpusTest {

	@Test
	public void test() {
		SplitWord crfSplitWord = MyStaticValue.getCRFSplitWord() ;
		for (String string : lines) {
			System.out.println(crfSplitWord.cut(string));
		}
	}

}
