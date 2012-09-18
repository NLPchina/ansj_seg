package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import love.cq.util.IOUtil;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class CSDNBLogTest {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/temp/blogBigFile.txt", "UTF-8");
		String content = null;
		ToAnalysis.paser("init") ;
		long start = System.currentTimeMillis();
		int size = 0;
		long length = 0;
		int i = 0;
		List<Term> all = null ;
		while ((content = reader.readLine()) != null) {
			i++;
			length += content.length();
			all = ToAnalysis.paser(content);
			size += all.size() ;
			if (i % 10000 == 0) {
				break;
			}
		}
		System.out.println(length);
		System.out.println(size);
		System.out.println((length*(long)1000)/(System.currentTimeMillis() - start));
		System.out.println(System.currentTimeMillis() - start);
		
	}

	public static void paser() {

	}
}
