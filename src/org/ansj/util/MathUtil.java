package org.ansj.util;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.InitDictionary;
import org.ansj.library.NatureLibrary;
import org.ansj.library.TwoWordLibrary;
import org.ansj.util.recognition.NatureRecognition.NatureTerm;

public class MathUtil {

	// 平滑参数
	private static final double dSmoothingPara = 0.1;
	// 一个参数
	private static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
	// ﻿Two linked Words frequency
	private static final double dTemp = (double) 1 / MAX_FREQUENCE;

	/**
	 * 从一个词的词性到另一个词的词性的分数
	 * 
	 * @param form
	 *            前面的词
	 * @param to
	 *            后面的词
	 * @return 分数
	 */
	public static double compuScore(Term from, Term to) {
		double frequency = from.getTermNatures().allFreq + 1;

		if (frequency < 0) {
			return from.getScore() + MAX_FREQUENCE;
		}

		int nTwoWordsFreq = TwoWordLibrary.getTwoWordFreq(from, to);
		double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara)
				* ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp));

		if (value < 0)
			value += frequency;

		if (value < 0) {
			value += frequency;
		}
		return from.getScore() + value;
	}

	/**
	 * 词性词频词长.计算出来一个分数
	 * 
	 * @param from
	 * @param term
	 * @return
	 */
	public static double compuScoreFreq(Term from, Term term) {
		// TODO Auto-generated method stub
		return from.getTermNatures().allFreq + term.getTermNatures().allFreq;
	}

	/**
	 * 两个词性之间的分数计算
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static double compuNatureFreq(NatureTerm from, NatureTerm to) {
		double twoWordFreq = NatureLibrary.getTwoNatureFreq(from.termNature.nature, to.termNature.nature);
		if (twoWordFreq == 0) {
			twoWordFreq = Math.log(from.selfScore + to.selfScore);
		}
		double score = from.score + Math.log((from.selfScore + to.selfScore) * twoWordFreq) + to.selfScore;
		return score;
	}

	/**
	 * 传入一个字符串.根据字符串的.字计算新词的可能性
	 * 
	 * @param name
	 * @param tn
	 * @return
	 */
	public static double scoreWord(String name, TermNatures tn) {
		int freq = 1  ;
		int smoothing = 1 ;
		if(tn.equals(TermNatures.NR)){
			smoothing = 5 ;
		}
		if(tn.equals(TermNatures.NT)){
			smoothing = 2 ;
		}
		double score = 0 ;
		
		for (int i = 0; i < name.length(); i++) {
			TermNatures termNatures = InitDictionary.termNatures[name.charAt(i)] ;
			if(termNatures==null){
				freq = 1 ;
			}else{
				freq = termNatures.allFreq ;
			}
			score += Math.log((freq+1)*dTemp) ;
		}
		return (score/name.length())*smoothing;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.log(dTemp*2));
	}
}
