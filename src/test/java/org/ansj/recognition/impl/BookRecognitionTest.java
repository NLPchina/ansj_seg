package org.ansj.recognition.impl;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

/**
 * Created by Ansj on 01/12/2017.
 */
public class BookRecognitionTest {

	@Test
	public void test() {


		String[] tests = new String[]{
				"歌曲《Звезда》是Макsим演唱的俄语歌曲。",
				"歌曲《Звезда》是Макsим演唱的俄语歌曲。《Звезда》",
				"《Звезда》歌曲《Звезда》是Макsим演唱的俄语歌曲。《Звезда》",
				"《Звезда》歌曲《Звезда》是Макsим演唱的俄语歌曲。《Звезда",
				"《Звезда》歌曲《《Звезда》是Макsим演唱的俄语歌曲。《Звезда",
				"《Звезда》歌曲《《Звезда》》是Макsим演唱的俄语歌曲。《Звезда",
		};

		for (String str : tests) {

			for (Term term : ToAnalysis.parse(str).recognition(new BookRecognition()).getTerms()) {
				System.out.print(term + " ");
			}
			System.out.println();
		}

	}
}
