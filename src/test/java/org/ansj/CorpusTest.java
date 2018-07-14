package org.ansj;

import org.junit.Before;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 初始化加载数据
 * 
 * @author Ansj
 *
 */
public class CorpusTest {

	public List<String> lines = null;

	@Before
	public void init() throws UnsupportedEncodingException, FileNotFoundException {
		lines = IOUtil.readFile2List("src/test/resources/badcase.txt", IOUtil.UTF8);
	}
	
}
