package org.ansj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ansj.dic.LearnTool;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.util.IOUtil;

public class Test2 {
	public static void main(String[] args) throws IOException {
		LearnTool learn = new LearnTool();
		NlpAnalysis analysis = new NlpAnalysis().setLearnTool(learn);

		for (File file : new File("/Users/sunjian/Downloads/szy/temp").listFiles()) {
			System.out.println("parse " + file.getName());
			String temp = null;
			BufferedReader reader = IOUtil.getReader(file, "gb2312");
			while ((temp = reader.readLine()) != null) {
				analysis.parseStr(temp);
			}
		}
		FileOutputStream fos = new FileOutputStream(new File("/Users/sunjian/Downloads/szy/result.txt"));
		learn.getTopTree(100000).forEach(e -> {
			try {
				fos.write((e.getKey() + "\t" + e.getValue() + "\n").getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		fos.flush();
		fos.close();
	}
}
