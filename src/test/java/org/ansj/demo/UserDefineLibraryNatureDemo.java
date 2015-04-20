package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

public class UserDefineLibraryNatureDemo {
	public static void main(String[] args) {
		//增加词汇
		UserDefineLibrary.insertWord("ansj大神", "作者", 1000);
		UserDefineLibrary.insertWord("eye.kuyun.com", "网站", 1000);
		
		List<Term> parse = ToAnalysis.parse("Ansj大神是eye.kuyun.com网站的开发者") ;
		
		System.out.println(parse);
		
		//自定义词性优先
		parse = FilterModifWord.modifResult(parse) ;
		
		System.out.println(parse);
	}
}
