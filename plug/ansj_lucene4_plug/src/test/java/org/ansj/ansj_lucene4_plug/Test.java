package org.ansj.ansj_lucene4_plug;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldValueFilter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.vectorhighlight.FieldQuery;

public class Test {
	public static void main(String[] args) {
		Term term=new Term("category","/technology/computers/programming");

		SpanQuery query=new SpanTermQuery(term);
		
		
		FieldValueFilter fvf = new FieldValueFilter("_id") ;
		
		FilteredQuery filteredQuery = new FilteredQuery(query, fvf) ;
		
		System.out.println(filteredQuery);
		
		System.out.println(fvf);
		System.out.println(query);

	}
}
