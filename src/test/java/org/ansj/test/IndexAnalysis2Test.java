package org.ansj.test;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.junit.Test;

public class IndexAnalysis2Test {
	
	@Test
	public void test(){

		UserDefineLibrary.insertWord("交通安全", "n", 2000);
		UserDefineLibrary.insertWord("交通", "n", 2000);
		UserDefineLibrary.insertWord("安全", "n", 2000);
		
		
		System.out.println(IndexAnalysis.parse("交通安全是根本交通安全"));;
	}
}
