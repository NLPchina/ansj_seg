package org.ansj.lucene.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.splitWord.Analysis;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public class AnsjTokenizer extends Tokenizer {
    // 当前词
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    // 偏移量
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    // 距离
    private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);

    protected Analysis ta = null;
    private Set<String> filter;
    private boolean pstemming;

    private final PorterStemmer stemmer = new PorterStemmer();

    public AnsjTokenizer(Analysis ta, Reader input, Set<String> filter, boolean pstemming) {
        super(input);
        this.ta = ta;
        this.filter = filter;
        this.pstemming = pstemming;
    }

    @Override
    public boolean incrementToken() throws IOException {
        // TODO Auto-generated method stub
        clearAttributes();
        int position = 0;
        Term term = null;
        String name = null;
        int length = 0;
        boolean flag = true;
        do {
            term = ta.next();
            if (term == null) {
                break;
            }
            name = term.getName();
            length = name.length();
            if (pstemming && term.getTermNatures().termNatures[0] == TermNature.EN) {
                name = stemmer.stem(name);
                term.setName(name);
            }

            if (filter != null && filter.contains(name)) {
                continue;
            } else {
                position++;
                flag = false;
            }
        } while (flag);

        if (term != null) {
            positionAttr.setPositionIncrement(position);
            termAtt.setEmpty().append(term.getName());
            offsetAtt.setOffset(term.getOffe(), term.getOffe() + length);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        ta.resetContent(new BufferedReader(this.input));
    }

}
