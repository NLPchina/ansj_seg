package org.ansj.lucene4;

import java.io.Reader;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public class AnsjAnalysis extends Analyzer {

    boolean            pstemming;
    public Set<String> filter;

    /**
     * @param filter 停用词
     * @param pstemming 是否分析词干
     */
    public AnsjAnalysis(Set<String> filter, boolean pstemming) {
        this.filter = filter;
    }

    /**
     * @param pstemming 是否分析词干.进行单复数,时态的转换
     */
    public AnsjAnalysis(boolean pstemming) {
        this.pstemming = pstemming;
    }

    public AnsjAnalysis() {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, final Reader reader) {
        // TODO Auto-generated method stub
        Tokenizer tokenizer = new AnsjTokenizer(new ToAnalysis(reader), reader, filter, pstemming);
        return new TokenStreamComponents(tokenizer);
    }

}
