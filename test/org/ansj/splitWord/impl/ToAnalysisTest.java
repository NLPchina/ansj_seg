package org.ansj.splitWord.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.library.TwoWordLibrary;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class ToAnalysisTest {
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		// File[] files = new
		// File("/Users/ansj/Documents/快盘/SogouCA.WWW08").listFiles() ;
		File[] files = { new File("/Users/ansj/Documents/快盘/冒死记录中国神秘事件（真全本）.txt") };
		int count = 0;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getName().endsWith(".txt")) {
				continue;
			}
			Reader reader = new InputStreamReader(new FileInputStream(files[i]), "GBK");
			Analysis toAnalysis = new ToAnalysis(reader);
			Term next = null;
			while ((next = toAnalysis.next()) != null) {
				// System.out.println(next.getName() + ":" + next.maxNature);
				// sb.append(next.getName()+":"+next.maxNature+"/ ") ;
				// sb.append("\n") ;
				// System.out.println(next.getName() +
				// "/"+next.getMaxPath().getNatureStr());
				if (next.getTermNatures().termNatures[0] == TermNature.NR) {
//					System.out.println(next.getName() + "\t" + next.selfScore);
					count++;
				}
			}
			// IOUtil.Writer("/Users/ansj/Documents/快盘/冒死.txt", "UTF-8",
			// sb.toString()) ;
		}
		System.out.println(count);
		System.out.println(System.currentTimeMillis() - start);

	}
}
