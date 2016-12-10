package org.ansj.dic.impl;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Jar2StreamTest {

	@Test
	public void test() throws IOException {
		InputStream stream = Jar2Stream.stream("jar://org.ansj.dic.DicReader|/crf.model") ;
		System.out.println(stream);
		stream.close();
	}

}
