package org.ansj.splitWord.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;
import org.ansj.util.recognition.NatureRecognition;

public class UserDefinedAnalysisTest {

	public static void main(String[] args) throws IOException {
		UserDefineLibrary.insertWord("我是特种兵","userDefine",100) ;
		
		System.out.println(ToAnalysis.paser("我是特种兵是一部很好看的电影!")); ;
		
	}
}
