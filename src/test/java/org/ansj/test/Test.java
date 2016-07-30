package org.ansj.test;

import java.io.IOException;
import java.util.HashSet;

import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class Test {

	public static void main(String[] args) throws IOException {
		HashSet<String> all = new HashSet<String>();
		
		all.add("我们在明略公司开会") ;

		long start = System.currentTimeMillis();

		for (String string : all) {
			System.out.println(string);
			System.out.println(ToAnalysis.parse(string));
			System.out.println(NlpAnalysis.parse(string));
		}
		System.out.println(System.currentTimeMillis() - start);

	}
}
