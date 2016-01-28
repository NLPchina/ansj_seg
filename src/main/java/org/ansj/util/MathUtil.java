package org.ansj.util;

import java.util.Map;

import org.ansj.domain.Term;
import org.ansj.library.NatureLibrary;
import org.ansj.library.NgramLibrary;
import org.ansj.recognition.NatureRecognition.NatureTerm;

public class MathUtil {

	// 平滑参数
	private static final double dSmoothingPara = 0.1;
	// 分隔符我最喜欢的
	private static final String TAB = "\t";
	// 一个参数
	private static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
	// ﻿Two linked Words frequency
	private static final double dTemp = (double) 1 / MAX_FREQUENCE;

	/**
	 * 从一个词的词性到另一个词的词的分数
	 * 
	 * @param form
	 *            前面的词
	 * @param to
	 *            后面的词
	 * @return 分数
	 */
	public static double compuScore(Term from, Term to, Map<String, Double> relationMap) {
		double frequency = from.termNatures().allFreq + 1;

		if (frequency < 0) {
			double score = from.score() + MAX_FREQUENCE;
			from.score(score);
			return score;
		}

		double nTwoWordsFreq = NgramLibrary.getTwoWordFreq(from, to);

		if (relationMap != null) {
			Double d = relationMap.get(from.getName() + TAB + to.getName());
			if (d != null) {
				nTwoWordsFreq += d;
			}
		}

		double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp));

		if (value < 0) {
			value += frequency;
		}
		return from.score() + value;
	}

	/**
	 * 词性词频词长.计算出来一个分数
	 * 
	 * @param from
	 * @param term
	 * @return
	 */
	public static double compuScoreFreq(Term from, Term term) {
		return from.termNatures().allFreq + term.termNatures().allFreq;
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

	public static void main(String[] args) {
		System.out.println(Math.log(dTemp * 2));
	}

}
