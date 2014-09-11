package org.ansj.demo;

import java.util.Arrays;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;

/**
 * 对非ansj的分词结果进行词性标注
 * @author ansj
 *
 */
public class NatureTagDemo {
	public static void main(String[] args) {
		String[] strs = {"对", "非", "ansj", "的", "分词", "结果", "进行", "词性", "标注"} ;
		
		List<String> lists = Arrays.asList(strs) ;
		List<Term> recognition = NatureRecognition.recognition(lists, 0) ;
		System.out.println(recognition);
	}
}
