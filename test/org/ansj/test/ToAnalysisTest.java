package org.ansj.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class ToAnalysisTest {
	public static void main(String[] args) throws IOException {
		
		// File[] files = new
		// File("/Users/ansj/Documents/快盘/SogouCA.WWW08").listFiles() ;
		File[] files = { new File("/Users/ansj/Downloads/西游记.txt") };
		int count = 0;
		ToAnalysis.parse("孙 123 sf") ;
		long start = System.currentTimeMillis();
		LearnTool learn = new LearnTool() ;
		
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getName().endsWith(".txt")) {
				continue;
			}
			Reader reader = new InputStreamReader(new FileInputStream(files[i]), "GBK");
			Analysis toAnalysis = new NlpAnalysis(reader,learn);
			Term next = null;
			while ((next = toAnalysis.next()) != null) {
				if("nw".equals(next.getNatrue().natureStr)){
					System.out.println(next);
				}
			}
		}
		System.out.println(learn.getTopTree(100,TermNatures.NW));
		System.out.println(System.currentTimeMillis() - start);

	}
}
