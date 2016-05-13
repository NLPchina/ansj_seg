package org.ansj.ansj_lucene5_plug;

import java.io.IOException;
import java.io.StringReader;

import org.ansj.library.UserDefineLibrary;
import org.ansj.lucene5.AnsjAnalyzer;
import org.ansj.lucene5.AnsjAnalyzer.TYPE;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

public class HeightLightTest {

	private static Directory directory = new RAMDirectory();
	
	private static Analyzer indexAnalyzer = new AnsjAnalyzer(TYPE.index);

	private static Analyzer queryAnalyzer = new AnsjAnalyzer(TYPE.index);

	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		
		UserDefineLibrary.insertWord("交通安全", "n", 2000);
		UserDefineLibrary.insertWord("交通", "n", 2000);
		UserDefineLibrary.insertWord("安全", "n", 2000);
		
		

		String content = "不强行上下车，做到先下后上，候车要排队，按秩序上车；下车后要等车辆开走后再行走，如要穿越马路，一定要确保安全的情况下穿行；交通信号灯的正确使用，什么事交通安全出行交通信号灯的正确使用，什么事交通安全出行";
		
		System.out.println(IndexAnalysis.parse(content));

		String query = "\"交通安全出行\"";


		// 建立内存索引对象
		index(indexAnalyzer, content);

		// 查询
		search(queryAnalyzer, new QueryParser("text", queryAnalyzer).parse(query));
	}

	private static void search(Analyzer analyzer, Query query) throws IOException {
		DirectoryReader directoryReader = DirectoryReader.open(directory);
		// 查询索引
		IndexSearcher isearcher = new IndexSearcher(directoryReader);
		System.out.println(query);
		TopDocs hits = isearcher.search(query, 5);
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int docId = hits.scoreDocs[i].doc;
			Document document = isearcher.doc(docId);
			System.out.println(toHighlighter(analyzer, query, document));
		}
	}

	/**
	 * 高亮设置
	 * 
	 * @param query
	 * @param doc
	 * @param field
	 * @return
	 */
	private static String toHighlighter(Analyzer analyzer, Query query, Document doc) {
		String field = "text";
		try {
			SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
			Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));
			TokenStream tokenStream1 = indexAnalyzer.tokenStream("text", new StringReader(doc.get(field)));
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

	private static void index(Analyzer analysis, String content) throws CorruptIndexException, IOException {
		Document doc = new Document();
		IndexWriter iwriter = new IndexWriter(directory, new IndexWriterConfig(analysis));
		doc.add(new Field("text", content, Field.Store.YES, Field.Index.ANALYZED));
		iwriter.addDocument(doc);
		iwriter.commit();
		iwriter.close();
	}
}
