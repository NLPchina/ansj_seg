package org.ansj.demo;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 词性标注
 * @author ansj
 *
 */
public class NatureDemo {
	public static void main(String[] args) throws IOException {
		List<Term> terms = ToAnalysis.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
		new NatureRecognition(terms).recognition() ;
		System.out.println(terms);
	}
}
