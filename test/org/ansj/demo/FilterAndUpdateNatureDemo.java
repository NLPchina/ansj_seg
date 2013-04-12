package org.ansj.demo;

import java.util.HashMap;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

public class FilterAndUpdateNatureDemo {
	public static void main(String[] args) {

		HashMap<String, String> updateDic = new HashMap<String, String>();

		// 用户自定义词性
		updateDic.put("停用词", "userDefine");

		// 停用词使用词性_stop
		updateDic.put("了", "_stop");

		// 第二种加停用词的方式
		updateDic.put("但是", FilterModifWord._stop);

		FilterModifWord.setUpdateDic(updateDic);

		List<Term> paser = ToAnalysis.paser("停用词过滤了.并且修正词性为用户自定义词性.但是你必须.must.设置停用词性词性词典");
		System.out.println(paser);

		paser = FilterModifWord.modifResult(paser);

		System.out.println(paser);

		/**
		 * 根据不同的预料是用不同的词典可以这么干
		 */

		updateDic.put("你", FilterModifWord._stop);
		updateDic.put("词典", "userDefine2");

		paser = ToAnalysis.paser("停用词过滤了.并且修正词性为用户自定义词性.但是你必须.must.设置停用词性词性词典");

		paser = FilterModifWord.modifResult(paser);

		System.out.println(paser);

	}
}
