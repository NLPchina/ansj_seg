package org.ansj.splitWord.impl;

import java.io.IOException;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.newWordFind.NewWordFind;

/**
 * 这是一个新词发现的简单例子.
 * @author ansj
 *
 */
public class NewWordFileFindDemo {
	public static void main(String[] args) throws IOException {
		String content = "java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础课件java基础";
		System.out.println(NewWordFind.getNewWords(content)); ;
	}
}	