package org.ansj.lucene3;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class NearTest {
	public static void createIndex() throws Exception {

		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_32, new AnsjAnalysis());
		Directory directory = FSDirectory.open(new File("c:/index"));
		IndexWriter writer = new IndexWriter(directory, conf);

		String str = "文化人;文化人谈文化";
		String[] values = str.split(";");
		for (String value : values) {
			Document doc = new Document();
			Field field = new Field("test", value, Store.YES, Index.ANALYZED_NO_NORMS, TermVector.WITH_POSITIONS_OFFSETS);
//			field.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			doc.add(field);
			writer.addDocument(doc);
			writer.commit();
		}

		writer.close();
	}

//	public static void createIndex() throws Exception {
//
//		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_32, new AnsjAnalysis());
//		Directory directory = FSDirectory.open(new File("index"));
//		IndexWriter writer = new IndexWriter(directory, conf);
//
//		String str = "文化人;文化人谈文化";
//		String[] values = str.split(";");
//		List<Document> docs = new ArrayList<Document>();
//		for (String value : values) {
//			Document doc = new Document();
//			Field field = new Field("test", value, Store.YES, Index.ANALYZED_NO_NORMS, TermVector.WITH_POSITIONS_OFFSETS);
//			// field.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
//			doc.add(field);
//			docs.add(doc);
//		}
//		writer.addDocuments(docs);
//		writer.commit();
//		writer.close();
//	}

	public static void testSearch() throws Exception {
		String rules = "\"文化人谈文化\"~10";
		IndexReader reader = IndexReader.open(FSDirectory.open(new File("index")));
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(Version.LUCENE_32, "test", new AnsjAnalysis());
		Query query = parser.parse(rules);
		System.out.println(query.toString());
		TopDocs topDocs = searcher.search(query, 10);
		ScoreDoc[] docs = topDocs.scoreDocs;
		System.out.println(docs.length);
		for (ScoreDoc scoreDoc : docs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Explanation explain = searcher.explain(query, scoreDoc.doc) ;
			System.out.println(explain);
			System.out.println(doc);
		}
	}
	
	public static void main(String[] args) throws Exception {
		createIndex() ;
		testSearch() ;
//		System.out.println(URLEncoder.encode("+"));
	}
}
