package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

public class FilterAndUpdateNatureDemo {
	public static void main(String[] args) {

		// 加入停用词
		FilterModifWord.insertStopWord("并且");
		FilterModifWord.insertStopWord("但是");

		// 加入过滤词性词性
		FilterModifWord.insertStopNatures("w","m","null") ;
		

		List<Term> parse = ToAnalysis.parse("停用词过滤了.并且修正词143922950性为用户自定义词性.但是你必须.must.设置停用词性词性词典");
		new NatureRecognition(parse).recognition();
		System.out.println(parse);
		
		UserDefineLibrary.insertWord("停用词", "userDefine", 1000);

		// 修正词性并且过滤停用
		parse = FilterModifWord.modifResult(parse);

		System.out.println(parse);

	}
}
