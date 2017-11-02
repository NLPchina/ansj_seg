package org.ansj.util;

/**
 * Created by Ansj on 01/11/2017.
 */

import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.File;
import java.util.Map;

/**
 * 词频统计
 */
public class Counter {

	/**
	 * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
	 * @param analysis 分词器构造
	 * @param size     统计的最大数字。超过数字做搜索 size*20% -》 size
	 * @param stop     过滤条件
	 * @return 分词后的统计结果
	 */
	public static Result count(File file, Analysis analysis, int size, StopRecognition stop) {

	}



	/**
	 * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
	 * @param analysis 分词器构造
	 * @param size     统计的最大数字。超过数字做搜索 size*20% -》 size
	 * @return 分词后的统计结果
	 */
	public static Result count(File file, Analysis analysis, int size) {
		count(file, analysis, size, null);
	}


	/**
	 * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
	 * @param analysis 分词器构造
	 * @return 分词后的统计结果
	 */
	public static Result count(File file, Analysis analysis) {
		count(file, analysis, 100000, null);
	}

	/**
	 * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
	 * @return 分词后的统计结果
	 */
	public static Result count(File file) {
		count(file, new ToAnalysis(), 100000, null);
	}

	public class Result {
		public Map<String, Double> map;

		public void writeToFile(String path, String encoding) {

		}
	}

}
