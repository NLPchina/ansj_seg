package org.ansj.splitWord.impl;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

/**
 * 词性标注的一个例子
 * @author ansj
 *
 */
public class NatureRecognitionTest {
	public static void main(String[] args) {
		String str = "他从马上掉了下来.我马上就下来了" ;
		List<Term> terms = ToAnalysis.paser(str) ;
		new NatureRecognition(terms).recogntion() ;
		System.out.println(terms);
	}
}
