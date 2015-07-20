package org.ansj.splitWord;

import org.ansj.Term;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本的分词.只做了.ngram模型.和数字发现.其他一律不管
 *
 * @author ansj
 */
public class BaseAnalysis extends Analysis {

    public BaseAnalysis(final Reader reader) {
        super(null);
        if (reader != null) {
            super.resetContent(new AnsjReader(reader));
        }
    }

    @Override
    protected List<Term> getResult(final Graph graph) {
        graph.walkPath();
        final List<Term> result = new ArrayList<>();
        int length = graph.terms.length - 1;
        for (int i = 0; i < length; i++) {
            if (graph.terms[i] != null) {
                result.add(graph.terms[i]);
            }
        }

        setRealName(graph, result);
        return result;
    }

    public static List<Term> parse(final String str) {
        return new BaseAnalysis(null).parseStr(str);
    }
}
