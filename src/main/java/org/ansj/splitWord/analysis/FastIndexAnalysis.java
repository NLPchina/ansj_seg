package org.ansj.splitWord.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.impl.GetWordsImpl;
import org.ansj.util.Graph;
import org.nlpcn.commons.lang.util.IOUtil;

public class FastIndexAnalysis extends Analysis {

	public static void main(String[] args) throws IOException {

		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/temp/test.txt", "utf-8");
		Term temp = null;

		System.out.println(FastIndexAnalysis.parse("河北省"));
		long start = System.currentTimeMillis();

		FastIndexAnalysis fia = new FastIndexAnalysis(reader);
		long length = 0;

		while ((temp = fia.next()) != null) {
			length += temp.getName().length();
		}

		System.out.println(length * 1000L / (System.currentTimeMillis() - start));

		System.out.println(System.currentTimeMillis() - start);

		System.out.println(length);

	}

	public FastIndexAnalysis(Reader br) {
		super.resetContent(br);
	}

	public FastIndexAnalysis() {
	}

	public static List<Term> parse(String str) {
		return new FastIndexAnalysis().parseStr(str);
	}

	@Override
	protected List<Term> getResult(Graph graph) {
		List<Term> result = new LinkedList<Term>();
		int length = graph.terms.length - 1;
		Term term = null;
		for (int i = 0; i < length; i++) {
			if ((term=graph.terms[i]) != null) {
				result.add(term);
				while((term =term.next())!=null){
					result.add(term);
				}
			}
		}

		return result;
	}

}
