package org.ansj.training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class RightRate {

	/**
	 * 准确率分析
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 读取分词文本
//		BufferedReader br = IOUtil.getReader("data/right_rate/gold_shanxi.txt", "UTF-8");
//		BufferedReader br = IOUtil.getReader("data/right_rate/msr_test_gold.txt", "UTF-8");
		BufferedReader br = IOUtil.getReader("data/right_rate/pku_test_gold.txt", "UTF-8");
		
		
		String temp_str = null;

		int line_number = 0;// 记录行数
		int ansj_term_number = 0;// 记录ansj中分出的term数量
		int result_num = 0;

		double P = 0.0;
		double R = 0.0;
		double F = 0.0;

		int allError = 0;
		int allSuccess = 0;

		// 记录每行分出来的字符串数组，分为两个，一个存语料库中的，一个存AnsjAnalyzer中分出来的
		String[] had_words_array = null;// 按split分开后的数组
		String body = null;
		String[] result = null;
		while ((temp_str = br.readLine()) != null) {
			int error = 0;
			int success = 0;
			body = temp_str.replace("  ", "");
			result = temp_str.split("  ");
			had_words_array = new String[body.length()];

			int offe = 0;

			// 填充result
			for (int i = 0; i < result.length; i++) {
				try {
					had_words_array[offe] = result[i];
					offe += result[i].length();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}

			List<Term> paser = ToAnalysis.paser(body);
			for (Term term : paser) {
				if (had_words_array[term.getOffe()] == null) {
					error++;
				} else if (had_words_array[term.getOffe()].equalsIgnoreCase(term.getName())) {
					success++;
				} else {
					success++;
				}
			}

			// ansj分出的个数
			ansj_term_number += paser.size();
			// 词语的个数
			result_num += result.length;
			// 累计错误数
			allError += error;
			// 累计正确数
			allSuccess += success;

			if(error>0){
				System.out.println("example:"+temp_str);
				System.out.println(" result:"+paser.toString().replace("[", "").replace("]", "").replace(", ", "  "));
			}
//			System.out.println("[" + line_number + "]---准确率P:--" + ((double) success / paser.size()));
			line_number++;
		}
		// 正确数/总词数
		P = allSuccess / (double) ansj_term_number;
		// 正确数/标注文本中的词数
		R = allSuccess / (double) result_num;

		F = (2 * P * R) / (P + R);
		System.out.println("P:" + P);
		System.out.println("R:" + R);
		System.out.println("全文平均准确率--" + F);
		br.close();
	}
}
