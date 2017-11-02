package org.ansj.ansj_lucene_plug;

import org.ansj.library.DicLibrary;
import org.ansj.lucene6.AnsjAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class PhraseTest {
	public static void main(String[] args) throws IOException, ParseException {

		DicLibrary.insert(DicLibrary.DEFAULT, "上网人");
		DicLibrary.insert(DicLibrary.DEFAULT, "网人");
		AnsjAnalyzer ansjAnalyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.index_ansj);
		TokenStream tokenStream = ansjAnalyzer.tokenStream("上网人员测试", "test");
		while (tokenStream.incrementToken()) {
			System.out.println(tokenStream.getAttribute(CharTermAttribute.class));
		}
		IndexWriterConfig config = new IndexWriterConfig(ansjAnalyzer);
		IndexWriter writer = new IndexWriter(new RAMDirectory(), config);
		Document doc = new Document();
		doc.add(new TextField("test", "上网人员测试", Field.Store.YES));
		writer.addDocument(doc);
		writer.commit();
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(writer));
		System.out.println(searcher.count(new TermQuery(new Term("test", "网人"))));
		Query q = new QueryParser("test", new AnsjAnalyzer(AnsjAnalyzer.TYPE.index_ansj)).parse("\"上网人\"");
		System.out.println(q);
		System.out.println(searcher.count(q));
	}
}
