package org.ansj.solr;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnsjTokenizerFactory extends TokenizerFactory {

	public final Logger logger = LoggerFactory.getLogger(getClass());

	private boolean pstemming;
	private boolean isQuery;
	private String stopwordsDir;
	public Set<String> filter;

	public AnsjTokenizerFactory(Map<String, String> args) {
		super(args);
		assureMatchVersion();
		isQuery = getBoolean(args, "isQuery", true);
		pstemming = getBoolean(args, "pstemming", false);
		stopwordsDir = get(args, "words");
		addStopwords(stopwordsDir);
	}

	/**
	 * 添加停用词
	 * 
	 * @param dir
	 */
	private void addStopwords(String dir) {
		if (dir == null) {
			logger.info("no stopwords dir");
			return;
		}
		logger.info("stopwords: {}", dir);
		filter = new HashSet<String>();
		File file = new File(dir);
		try (FileInputStream fis = new FileInputStream(file)) {
			InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String word = br.readLine();
			while (word != null) {
				filter.add(word);
				word = br.readLine();
			}
		} catch (FileNotFoundException e) {
			logger.info("No stopword file found");
		} catch (IOException e) {
			logger.info("stopword file io exception");
		}
	}

	@Override
	public Tokenizer create(AttributeFactory factory, Reader input) {
		if (isQuery == true) {
			return new AnsjTokenizer(new ToAnalysis(input), input, filter, pstemming);
		} else {
			return new AnsjTokenizer(new IndexAnalysis(input), input, filter, pstemming);
		}
	}
}