package org.ansj.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class ToAnalysisTest {
	public static void main(String[] args) throws IOException {
		
		// File[] files = new
		// File("/Users/ansj/Documents/快盘/SogouCA.WWW08").listFiles() ;
		File[] files = { new File("/Users/ansj/Documents/快盘/分词/红楼梦.txt") };
		int count = 0;
		ToAnalysis.paser("孙 123 sf") ;
		long start = System.currentTimeMillis();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getName().endsWith(".txt")) {
				continue;
			}
			Reader reader = new InputStreamReader(new FileInputStream(files[i]), "GBK");
			Analysis toAnalysis = new ToAnalysis(reader);
			Term next = null;
			while ((next = toAnalysis.next()) != null) {
			}
		}
		System.out.println(System.currentTimeMillis() - start);

	}
}
