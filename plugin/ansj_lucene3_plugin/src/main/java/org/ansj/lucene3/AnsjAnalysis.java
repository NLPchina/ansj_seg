package org.ansj.lucene3;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class AnsjAnalysis extends Analyzer {
	public Set<String> filter;

	public boolean pstemming = false;;

	/**
	 * 如果需要停用词就传入停用词的hashmap value0
	 * 
	 * @param filter
	 * @param pstemming
	 *            ,是否分析词干
	 */
	public AnsjAnalysis(Set<String> filter, boolean pstemming) {
		this.filter = filter;
		this.pstemming = pstemming;
	}

	public AnsjAnalysis(boolean pstemming) {
		this.pstemming = pstemming;
	}

	public AnsjAnalysis() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		// TODO Auto-generated method stub
		return new AnsjTokenizer(new ToAnalysis(new BufferedReader(reader)), reader, filter, pstemming);
	}

}
