package org.ansj.ansj_lucene_plug;

import org.ansj.lucene7.AnsjAnalyzer;
import org.ansj.lucene7.AnsjAnalyzer.TYPE;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.StringReader;

public class HeightLightTest2 {

	private static Directory directory = new RAMDirectory();

	private static Analyzer indexAnalyzer = new AnsjAnalyzer(TYPE.index_ansj);

	private static Analyzer queryAnalyzer = new AnsjAnalyzer(TYPE.index_ansj);

	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		String content = "<p><strong><img class=\"__bg_gif\" data-ratio=\"0.13125\"  class=\"aligncenter\" src=\"https://nicolcdn.com/rmt_wx/mmbiz_gif/altnl8Xibvb0Hs1a6RRMViagjfh8eGZ46oD71VPPZyOwvxD9NgLmHpAluRVKfRBNohHPxpqtjX7AyUaS2wIuVclA/0?wx_fmt=gif\" data-type=\"gif\" data-w=\"640\" style=\"box-sizing: border-box !important; word-wrap: break-word !important; visibility: visible !important; width: auto !important;\" width=\"auto\"></strong></p><p><strong><br></strong></p><p><strong><span style=\"color: rgb(255, 41, 65);\">韩国YG娱乐梁铉锡</span></strong><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\">代表通过个人SNS公开了男团</span></strong><strong><span style=\"color: rgb(255, 41, 65);\">WINNER新专辑双主打之一《REALLY REALLY》的预告片。</span></strong></p><p></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\"><br></span></strong></p><p><strong><img data-ratio=\"1.4096153846153847\" data-s=\"300,640\"  class=\"aligncenter\" src=\"https://nicolcdn.com/rmt_wx/mmbiz_jpg/altnl8Xibvb3NeY5uGiayGhuf2EUiccQycHDFibbc0Hl3KmeW9dI0tAB2nrDpxxcdkS6PrveIz4WTeGiaRPAz5Vb5cQ/0?wx_fmt=jpeg\" data-type=\"jpeg\" data-w=\"520\"></strong></p><p><strong><br></strong></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\">&nbsp;&nbsp;这也是</span></strong><strong><span style=\"color: rgb(255, 41, 65);\">WINNER时隔1年2个月正式回归乐坛</span></strong><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\">。而4月4日即将公开的作为WINNER其中一首主打曲《really really》的mv。虽然画面公开只有短短的10秒钟，但抓耳的旋律一下就吸引了粉丝的目光。</span></strong><strong style=\"color: rgb(255, 41, 65); font-size: 16px; text-align: center; line-height: 1.6;\">话不多说，先献上此次专辑的teaser供大家舔屏先......</strong></p><p><strong style=\"color: rgb(255, 41, 65); font-size: 16px; text-align: center; line-height: 1.6;\"><br></strong></p><p><strong style=\"color: rgb(255, 41, 65); font-size: 16px; text-align: center; line-height: 1.6;\"><iframe allowfullscreen=\"\" class=\"video_iframe\" src=\"https://v.qq.com/iframe/preview.html?vid=k038795jxlj&amp;width=500&amp;height=375&amp;auto=0\" data-vidtype=\"1\" frameborder=\"0\" height=\"417\" style=\"z-index:1;\" width=\"556\"></iframe><br></strong></p><p style=\"text-align: center;\"></p><p></p><p style=\"text-align: center;\"><span style=\"font-size: 16px; color: rgb(255, 41, 65);\"><strong><br></strong></span></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\">《REALLY REALLY》利用了近日流行的Tropical风格，是一首旋律轻快的歌曲。而在视频最后出现的一句“喜欢你”的歌词与旋律完美地融合在了一起，令粉丝对WINNER的新曲期待不已。</span></strong></p><p></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\"><br></span></strong></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\"></span></strong></p><p><img data-ratio=\"0.412962962962963\"  class=\"aligncenter\" src=\"https://nicolcdn.com/rmt_wx/mmbiz_gif/altnl8Xibvb3NeY5uGiayGhuf2EUiccQycHlVOnG8wQM4zbcaia0aQb68DSUzmgiapLHbtM13RzNs79ibaRQYEMEfE8g/0?wx_fmt=gif\" data-type=\"gif\" data-w=\"540\"></p><p><br></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\">　另外，WINNER的新曲《REALLY REALLY》<span style=\"color: rgb(255, 41, 65); font-size: 16px;\">完整版MV预告</span>将于今日下午4点（韩国时间）公开。</span></strong><strong><span style=\"color: rgb(255, 41, 65);\">喜欢温拿的朋友们不要错过了哟~！</span></strong></p><p></p><p><strong><span style=\"font-size: 14px; color: rgb(123, 12, 0);\"><br></span></strong></p><p><img data-ratio=\"0.7603448275862069\"  class=\"aligncenter\" src=\"https://nicolcdn.com/rmt_wx/mmbiz_gif/altnl8Xibvb3NeY5uGiayGhuf2EUiccQycHCaAaWdstCDqxIfqYWhKRMe1CQaGp6XjPjeede0j3PlHTuyxJcpxPgw/0?wx_fmt=gif\" data-type=\"gif\" data-w=\"580\"></p><hr style=\"max-width: 100%; color: rgb(62, 62, 62); font-size: 16px; line-height: 25.6px; white-space: normal; box-sizing: border-box !important; word-wrap: break-word !important; background-color: rgb(255, 255, 255);\"><p><br style=\"max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;\"></p><p><strong><img class=\"__bg_gif\" data-ratio=\"0.5875\"  class=\"aligncenter\" src=\"https://nicolcdn.com/rmt_wx/mmbiz_gif/altnl8Xibvb2I3HJ6GDuC8bS72fUNIDjorThJFmWDEur0wGx9jl5tHdn76E2k4WiaM1xuo0uibuPLTpiaPIQ5pXNpQ/0?wx_fmt=gif\" data-type=\"gif\" data-w=\"640\" style=\"line-height: 25.6px; box-sizing: border-box !important; word-wrap: break-word !important; visibility: visible !important; width: auto !important;\" width=\"auto\"></strong></p><p style=\"font-size: 16px; line-height: 25.6px; white-space: normal; max-width: 100%; min-height: 1em; color: rgb(62, 62, 62); text-align: center; box-sizing: border-box !important; word-wrap: break-word !important; background-color: rgb(255, 255, 255);\"><strong style=\"max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;\"><span style=\"max-width: 100%; color: rgb(255, 41, 65); box-sizing: border-box !important; word-wrap: break-word !important;\">20170331今日热点&nbsp;</span></strong></p><p style=\"font-size: 16px; line-height: 25.6px; white-space: normal; max-width: 100%; min-height: 1em; color: rgb(62, 62, 62); text-align: center; box-sizing: border-box !important; word-wrap: break-word !important; background-color: rgb(255, 255, 255);\"><strong style=\"max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;\"><span style=\"max-width: 100%; color: rgb(255, 41, 65); box-sizing: border-box !important; word-wrap: break-word !important;\">点击左下方【阅读原文】即可查看！&nbsp;</span></strong></p><p style=\"font-size: 16px; line-height: 25.6px; white-space: normal; max-width: 100%; min-height: 1em; color: rgb(62, 62, 62); text-align: center; box-sizing: border-box !important; word-wrap: break-word !important; background-color: rgb(255, 255, 255);\"><strong style=\"max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;\"><span style=\"max-width: 100%; color: rgb(255, 41, 65); box-sizing: border-box !important; word-wrap: break-word !important;\">↓↓↓↓↓↓↓</span></strong></p><p><br></p>\n" ;


		String query = "text:\"really\"";

		// 建立内存索引对象
		index(indexAnalyzer, content);

		// 查询
		search(queryAnalyzer, new QueryParser("text", queryAnalyzer).parse(query));
	}

	private static void search(Analyzer analyzer, Query query) throws IOException {
		DirectoryReader directoryReader = DirectoryReader.open(directory);
		// 查询索引
		IndexSearcher isearcher = new IndexSearcher(directoryReader);
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
	 * @return
	 */
	private static String toHighlighter(Analyzer analyzer, Query query, Document doc) {
		String field = "text";
		try {
			SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
			Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));
			highlighter.setTextFragmenter(new SimpleFragmenter(1000));
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
		doc.add(new TextField("text", content, Field.Store.YES));
		iwriter.addDocument(doc);
		iwriter.commit();
		iwriter.close();
	}
}
