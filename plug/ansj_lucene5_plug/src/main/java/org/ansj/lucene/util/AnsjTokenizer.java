package org.ansj.lucene.util;

import org.ansj.Term;
import org.ansj.TermNatures;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.AnsjReader;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.Set;

public class AnsjTokenizer extends Tokenizer {

    /**
     * 当前词
     */
    private final CharTermAttribute termAtt;
    /**
     * 偏移量
     */
    private final OffsetAttribute offsetAtt;
    /**
     * 距离
     */
    private final PositionIncrementAttribute positionAttr;
    protected final Analysis ta;
    private final Set<String> filter;
    private final boolean pstemming;
    private final PorterStemmer stemmer;

    public AnsjTokenizer(
            final Analysis ta,
            final Set<String> filter,
            final boolean pstemming
    ) {
        this.termAtt = addAttribute(CharTermAttribute.class);
        this.offsetAtt = addAttribute(OffsetAttribute.class);
        this.positionAttr = addAttribute(PositionIncrementAttribute.class);
        this.ta = ta;
        this.filter = filter;
        this.pstemming = pstemming;
        this.stemmer = new PorterStemmer();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        super.clearAttributes();

        int position = 0;
        do {
            final Term term = this.ta.next();
            if (term == null) {
                return false;
            }
            final int nameLength = term.getName().length();
            final String name = !this.pstemming || term.getTermNatures() != TermNatures.EN ?
                    term.getName() :
                    this.stemmer.stem(term.getName());

            if (this.filter == null || !this.filter.contains(name)) {
                position++;
                this.positionAttr.setPositionIncrement(position);
                this.termAtt.setEmpty().append(name);
                this.offsetAtt.setOffset(term.getOffe(), term.getOffe() + nameLength);
                return true;
            }
        } while (true);
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        this.ta.resetContent(new AnsjReader(this.input));
    }
}
