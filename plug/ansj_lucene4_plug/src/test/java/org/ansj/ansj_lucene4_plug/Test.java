package org.ansj.ansj_lucene4_plug;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;

public class Test {
	public static void main(String[] args) {
		Term term=new Term("category","/technology/computers/programming");

		SpanQuery query=new SpanTermQuery(term);
		
		System.out.println(query);

	}
}
