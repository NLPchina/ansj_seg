package org.ansj.test;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class NlpTest {
	public static void main(String[] args) {
		List<Term> paser = NlpAnalysis.paser("捐款额达到200元") ;
		System.out.println(paser);
	}
}
