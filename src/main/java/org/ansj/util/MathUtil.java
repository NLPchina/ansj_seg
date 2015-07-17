package org.ansj.util;

import org.ansj.domain.Term;
import org.ansj.library.NatureLibrary;
import org.ansj.library.NgramLibrary;
import org.ansj.recognition.NatureRecognition.NatureTerm;

import static org.ansj.util.MyStaticValue.NATURE_LIBRARY;

public class MathUtil {

	// 平滑参数
	private static final double dSmoothingPara = 0.1;
	// 一个参数
	private static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
	// ﻿Two linked Words frequency
	private static final double dTemp = (double) 1 / MAX_FREQUENCE;

	/**
	 * 从一个词的词性到另一个词的词的分数
	 * 
	 * @param from 前面的词
	 * @param to   后面的词
	 * @return 分数
	 */
	public static double compuScore(final Term from, final Term to) {
		double frequency = from.termNatures().allFreq + 1;

		if (frequency < 0) {
			double score = from.score() + MAX_FREQUENCE;
			from.score(score);
			return score;
		}

		int nTwoWordsFreq = NgramLibrary.getTwoWordFreq(from, to);
		double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp));

		if (value < 0) {
			value += frequency;
		}
		return from.score() + value;
	}

	/**
	 * 词性词频词长.计算出来一个分数
	 * 
	 * @param from from
	 * @param term
	 * @return
	 */
	public static double compuScoreFreq(final Term from, final Term term) {
		return from.termNatures().allFreq + term.termNatures().allFreq;
	}

	/**
	 * 两个词性之间的分数计算
	 * 
	 * @param from from
	 * @param to   to
	 * @return    score
	 */
	public static double compuNatureFreq(final NatureTerm from, final NatureTerm to) {
		double twoWordFreq = NATURE_LIBRARY.getNatureFreq(from.termNature.nature, to.termNature.nature);
		if (twoWordFreq == 0) {
			twoWordFreq = Math.log(from.selfScore + to.selfScore);
		}
		return from.score + Math.log((from.selfScore + to.selfScore) * twoWordFreq) + to.selfScore;
	}

	public static void main(String[] args) {
		System.out.println(Math.log(dTemp * 2));
	}
}
