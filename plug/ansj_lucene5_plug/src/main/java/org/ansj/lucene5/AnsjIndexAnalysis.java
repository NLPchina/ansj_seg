package org.ansj.lucene5;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.IndexAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.Reader;
import java.util.Set;

public class AnsjIndexAnalysis extends Analyzer {

    private final boolean pstemming;
    private final Set<String> filter;

//    /**
//     * @param pstemming 是否分析词干.进行单复数,时态的转换
//     */
//    public AnsjIndexAnalysis(final boolean pstemming) {
//        this.filter = null;
//        this.pstemming = pstemming;
//    }
//
//    public AnsjIndexAnalysis() {
//        super();
//        this.filter = null;
//        this.pstemming = false;
//    }

    /**
     * @param filter    停用词
     * @param pstemming 是否分析词干.进行单复数,时态的转换
     */
    public AnsjIndexAnalysis(final Set<String> filter, final boolean pstemming) {
        this.filter = filter;
        //this.pstemming = false;
        this.pstemming = pstemming;
    }

    private volatile Reader reader;

    @Override
    protected Reader initReader(final String fieldName, final Reader reader) {
        this.reader = reader;
        return reader;
    }

    @Override
    protected final TokenStreamComponents createComponents(final String fieldName) {
        Tokenizer tokenizer = new AnsjTokenizer(new IndexAnalysis(this.reader, null), this.filter, this.pstemming);
        return new TokenStreamComponents(tokenizer);
    }
}
