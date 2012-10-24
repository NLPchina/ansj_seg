package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

public class TestFile3 {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = IOUtil.getReader("d:/a/zbn.txt", "GBK") ;
		String temp = null ;
		while((temp=reader.readLine())!=null){
			Term term = null;
			List terms = ToAnalysis.paser(temp) ;
			new NatureRecognition(terms).recogntion();
			for (int i = 0; i < terms.size(); i++) {
				if (((Term) terms.get(i)).getNatrue().natureStr.equals("nr")) {
					if (i != 0) {
						if (((Term) terms.get(i - 1)).getNatrue().natureStr
								.equals("n")
								&& countChineseCharacter(((Term) terms.get(i - 1)).getName()) > 1) {
							System.out.println(((Term)terms.get(i)).getName() + "===get-i-1: "
									+ ((Term) terms.get(i - 1)).getName());
						}
					}
					if (((Term) terms.get(i + 1)).getNatrue().natureStr.equals("n")
							&& countChineseCharacter(((Term) terms.get(i + 1)).getName()) > 1) {
						System.out.println(((Term)terms.get(i)).getName() + "====get-i+1: "
								+ ((Term) terms.get(i + 1)).getName());
					}
				}
			}
		}
	}
	
	static int countChineseCharacter(String s) {
		int count = 0;
		Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(s);
		while (matcher.find()) {
			count++;
		}
		return count;
	}
}
