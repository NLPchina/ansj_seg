package org.ansj.splitWord.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import love.cq.library.Library;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;

public class UserDefinedAnalysisTest {

	public static void main(String[] args) throws IOException {

		// UserDefineLibrary.insertWord("我是特种兵","userDefine",100) ;
		//
		// System.out.println(ToAnalysis.paser("我是特种兵是一部很好看的电影!")); ;

		String format = "%s\tuserDefine\t1000";
		List<String> dic = new ArrayList<String>();
		
		dic.add("我是特种兵") ;
		for (int i = 0; i < dic.size(); i++) {
			Library.insertWord(UserDefineLibrary.FOREST, String.format(format, new Object[] { dic.get(i) }));
		}
		
		System.out.println(ToAnalysis.paser("我是特种兵是一部很好看的电影!"));

	}
}
