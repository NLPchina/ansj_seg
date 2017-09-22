package org.ansj.app.extracting;

import org.ansj.app.extracting.exception.RuleFormatException;
import org.junit.Test;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Ansj on 21/09/2017.
 */
public class ExtractingTest {

	@Test
	public void test() throws IOException, RuleFormatException {

		new Extracting(LexicalTest.class.getResourceAsStream("/rule.txt"),"utf-8").parse("2017年2月12日");


	}
}
