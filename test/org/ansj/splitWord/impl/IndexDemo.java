package org.ansj.splitWord.impl;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.IndexAnalysis;

/**
 * 针对索引的分词方式
 * 
 * @author ansj
 * 
 */
public class IndexDemo {
	public static void main(String[] args) throws IOException {
		int all = 0;
		String str = "演艺圈为上位衣服都可以扒了我们假报一年的工作经验你以为我们脸不红么？但是我们这些刚出来的也要吃饭啊，也要有个机会让前辈知道我们是可以用的不是废品啊？不假报连大门都进不去还谈什么工作，反正我在简历里面把我在培训的时间算在工作经验里面了，楼下的想骂就骂吧....可耻但是我不能委屈了肚子，看了有些人的技术都不如我的同学都有……";
		long start = System.currentTimeMillis();

		for (int i = 0; i < 1; i++) {
			all += str.length();
			List<Term> paser = IndexAnalysis.paser(str);

			for (Term term : paser) {
				System.out.println(term + "\t" + term.getOffe());
			}
		}
		System.out.println("每秒处理了:" + (all * 1000.0 / (System.currentTimeMillis() - start)));
	}
}
