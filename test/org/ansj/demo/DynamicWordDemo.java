package org.ansj.demo;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

/**
 * 动态添加删除用户自定义词典!
 * 
 * @author ansj
 * 
 */
public class DynamicWordDemo {
	public static void main(String[] args) {
		// 增加新词,中间按照'\t'隔开
		UserDefineLibrary.insertWord("ansj中文分词", "userDefine", 1000);
		List<Term> terms = ToAnalysis.paser("我觉得ansj中文分词是一个不错的系统!我是王婆!");
		System.out.println("增加新词例子:" + terms);

		// 删除词语,只能删除.用户自定义的词典.
		UserDefineLibrary.removeWord("ansj中文分词");
		terms = ToAnalysis.paser("我觉得ansj中文分词是一个不错的系统!我是王婆!");
		System.out.println("删除用户自定义词典例子:" + terms);
	}
}
