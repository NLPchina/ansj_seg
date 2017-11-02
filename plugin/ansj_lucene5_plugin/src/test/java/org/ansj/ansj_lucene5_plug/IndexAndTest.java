package org.ansj.ansj_lucene5_plug;

import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.lucene5.AnsjAnalyzer;
import org.ansj.lucene5.AnsjAnalyzer.TYPE;
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

		String text = "[工程名称]赣州市南康区第四中学学生公寓、食堂及附属工程\n" + 
				"[关键信息]赣州市南康区第四中学.; 房屋建筑工 程施工总承包叁级以上(含叁级)资质;无;本工程授权委托人(注册建造师)须提供劳动合同和投标公司为其缴交的社保证明（社保证明时间为2015 年 11 月至 2016 年 1 月）原件，须提供加盖当地社保局业务章的社保手册或花名册(含姓名、社保查询号或身份证号、缴费基数和缴费凭证)或基本养老保险个人帐户对账单；如果建造师是法人代表的，则提供：身份证、法人代表资格证、建造师注册证书及其相应的 B 类安全生产考核合格证；               须提交公司或投标项目所 在地的检察机关出具的投标公司和投标公 司拟派项目负责人的《关于行贿犯罪档案 查询通知书》 。;小胡开银诚、中灿两家，赣州市南康区第四中学学生公寓、食堂及附属工程，本项目投资 857.9 万元，开标时间：2016 年 03 月 01 日 10:00，投标保证金的金额：15 万元，保证金到账截止时间为 2016 年 2月 26 日 17:00 时。介绍信2000元/家，报名费600元/家，保证金老板自己打。开标老板：黄思婷 134-0707-4912    委托人：胡童科。;2000.0;\n" + 
				"[其他信息]赣州分公司;赣州分公司;投标申请单-20160226-1;3607821602050117-1.JXZF;;";

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
