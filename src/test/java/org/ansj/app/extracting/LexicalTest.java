package org.ansj.app.extracting;

import org.ansj.app.extracting.exception.RuleFormatException;
import org.junit.Test;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Ansj on 29/08/2017.
 */
public class LexicalTest {
	@Test
	public void test() throws IOException, RuleFormatException {
		BufferedReader br = IOUtil.getReader(LexicalTest.class.getResourceAsStream("/rule.txt"), "utf-8");
		String temp = null ;
		while((temp=br.readLine())!=null){
			if(StringUtil.isBlank(temp)){
				continue;
			}
			System.out.println(Lexical.parse(temp)); ;
		}


	}
}
