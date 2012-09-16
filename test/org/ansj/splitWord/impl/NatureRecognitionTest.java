package org.ansj.splitWord.impl;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

/**
 * 词性标注的一个例子
 * 
 * @author ansj
 * 
 */
public class NatureRecognitionTest {
	public static void main(String[] args) {
		String str = "结婚的和尚未结婚的孙建是一个好人";
		List<Term> terms = ToAnalysis.paser(str);
		new NatureRecognition(terms).recogntion();
	}
}
