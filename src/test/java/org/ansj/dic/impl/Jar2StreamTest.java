package org.ansj.dic.impl;

import org.ansj.dic.PathToStream;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class Jar2StreamTest {

	@Test
	public void test() throws IOException {
		InputStream stream = PathToStream.stream("jar://org.ansj.dic.DicReader|/crf.model") ;
		System.out.println(stream);
		stream.close();
	}

}
