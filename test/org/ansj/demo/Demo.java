package org.ansj.demo;

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
		Analysis udf = new ToAnalysis(new StringReader("孙健用java重写了张华平老师的分词."));
		Term term = null ;
		while((term=udf.next())!=null){
			System.out.print(term.getName()+" ");
		}
	}
}
