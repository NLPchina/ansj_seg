package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;

import org.ansj.util.MyStaticValue;
import org.junit.Test;
import org.nlpcn.commons.lang.util.IOUtil;

public class SplitWordTest {
	
	@Test
	public void cohesionTest() throws IOException{
		BufferedReader reader = IOUtil.getReader(MyStaticValue.userLibrary, IOUtil.UTF8) ;
		
		String temp = null ;
		
		long start = System.currentTimeMillis() ;
		while((temp=reader.readLine())!=null){
			String word = temp.split("\t")[0] ;
			double value = MyStaticValue.getCRFSplitWord().cohesion(word) ;
		}
		System.out.println(System.currentTimeMillis()-start);
	}
}
