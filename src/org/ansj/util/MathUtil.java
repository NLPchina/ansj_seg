package org.ansj.util;

import java.util.List;

import org.ansj.domain.NewWordNatureAttr;
import org.ansj.domain.Term;
import org.ansj.library.NatureLibrary;
import org.ansj.library.NgramLibrary;
import org.ansj.recognition.NatureRecognition.NatureTerm;

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

		int nTwoWordsFreq = NgramLibrary.getTwoWordFreq(from, to);
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


	public static void main(String[] args) {
		System.out.println(Math.log(dTemp * 2));
	}

	/**
	 * 新词熵及其左右熵
	 * 
	 * @param all
	 */
	public static double leftRightEntropy(List<Term> all) {
		// TODO Auto-generated method stub
		double score = 0;
		NewWordNatureAttr newWordAttr = null;
		Term first = all.get(0);
		

		// 查看左右链接
		int twoWordFreq = NgramLibrary.getTwoWordFreq(first.getFrom(), first);
		score -= twoWordFreq;
		

		// 查看右连接
		int length = all.size() - 1;
		Term end = all.get(all.size() - 1);
		twoWordFreq = NgramLibrary.getTwoWordFreq(end, end.getTo());
		score -= twoWordFreq;
		

		// 查看内部链接
		for (int i = 0; i < length; i++) {
			score -= NgramLibrary.getTwoWordFreq(all.get(i), all.get(i + 1));
		}
		if (score < -3) {
			return 0;
		}
		

		// 首字分数
		newWordAttr = first.getTermNatures().newWordAttr;
		score += getTermScore(newWordAttr, newWordAttr.getB());
		// 末字分数
		newWordAttr = end.getTermNatures().newWordAttr;
		score += getTermScore(newWordAttr, newWordAttr.getE());
		// 中词分数
		double midelScore = 0 ;
		Term term = null ;
		for (int i = 1; i < length ; i++) {
			term = all.get(i) ;
			newWordAttr = term.getTermNatures().newWordAttr;
			midelScore += getTermScore(newWordAttr, newWordAttr.getM());
		}
		score +=  midelScore/(length) ;
		return score;
	}

	private static double getTermScore(NewWordNatureAttr newWordAttr, int freq) {
		if(newWordAttr==NewWordNatureAttr.NULL){
			return 3 ;
		}
		return (freq / (double) (newWordAttr.getAll() + 1)) * Math.log(500000 / (double) (newWordAttr.getAll() + 1));
	}
	

}
