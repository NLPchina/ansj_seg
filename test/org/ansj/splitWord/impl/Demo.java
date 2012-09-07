package org.ansj.splitWord.impl;

import java.io.IOException;
import java.io.StringReader;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 标注的分词方式,这里面的流你可以传入任何流.除了流氓
 * @author ansj
 *
 */
public class Demo {
	public static void main(String[] args) throws IOException {
		Analysis udf = new ToAnalysis(new StringReader("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!"));
		Term term = null ;
		while((term=udf.next())!=null){
			System.out.print(term.getName()+" ");
		}
	}
}
