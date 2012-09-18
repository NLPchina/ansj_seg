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
		Analysis udf = new ToAnalysis(new StringReader("通过收购Fulcrum Microsystems和SDN服务商WindRiver，Intel建立了自己的SDN架构，SDN被视为下一代数据中心不可或缺的技术。此外，Intel积极加入开源社区和联盟，巩固了自己在数据中心的地位。"));
		Term term = null ;
		while((term=udf.next())!=null){
			System.out.print(term.getName()+" ");
		}
	}
}
