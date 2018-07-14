package org.ansj.ansj_lucene_plug;

import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.lucene6.AnsjAnalyzer;
import org.ansj.lucene6.AnsjAnalyzer.TYPE;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class IndexAndTest {

	@Test
	public void test() throws Exception {
		DicLibrary.put(DicLibrary.DEFAULT, "../../library/default.dic");
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new AnsjAnalyzer(TYPE.index_ansj));
		Directory directory = null;
		IndexWriter iwriter = null;

		IndexWriterConfig ic = new IndexWriterConfig(analyzer);

		String text = "旅游和服务是最好的";

		System.out.println(IndexAnalysis.parse(text));

		// 建立内存索引对象
		directory = new RAMDirectory();
		iwriter = new IndexWriter(directory, ic);
		addContent(iwriter, text);
		iwriter.commit();
		iwriter.close();

		System.out.println("索引建立完毕");

		Analyzer queryAnalyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.index_ansj);

		System.out.println("index ok to search!");

		for (Term t : IndexAnalysis.parse(text)) {
			System.out.println(t.getName());
			search(queryAnalyzer, directory, "\"" + t.getName() + "\"");
		}

	}

	private void search(Analyzer queryAnalyzer, Directory directory, String queryStr) throws CorruptIndexException, IOException, ParseException {
		IndexSearcher isearcher;
		DirectoryReader directoryReader = DirectoryReader.open(directory);
		// 查询索引
		isearcher = new IndexSearcher(directoryReader);
		QueryParser tq = new QueryParser("text", queryAnalyzer);
		Query query = tq.parse(queryStr);
		System.out.println(query);
		TopDocs hits = isearcher.search(query, 5);
		System.out.println(queryStr + ":共找到" + hits.totalHits + "条记录!");
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int docId = hits.scoreDocs[i].doc;
			Document document = isearcher.doc(docId);
			System.out.println(toHighlighter(queryAnalyzer, query, document));
		}
	}

	private void addContent(IndexWriter iwriter, String text) throws CorruptIndexException, IOException {
		Document doc = new Document();
		IndexableField field = new TextField("text", text, Store.YES);
		doc.add(field);
		iwriter.addDocument(doc);
	}

	/**
	 * 高亮设置
	 * 
	 * @param query
	 * @param doc
	 * @param field
	 * @return
	 */
	private String toHighlighter(Analyzer analyzer, Query query, Document doc) {
		String field = "text";
		try {
			SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
			Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));
			TokenStream tokenStream1 = analyzer.tokenStream("text", new StringReader(doc.get(field)));
			String highlighterStr = highlighter.getBestFragment(tokenStream1, doc.get(field));
			return highlighterStr == null ? doc.get(field) : highlighterStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
