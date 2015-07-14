package org.ansj.splitWord.analysis;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;

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
        return new Merger() {

            @Override
            public List<Term> merger() {
                graph.walkPath();
                return getResult();
            }

            private List<Term> getResult() {
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
        }.merger();
    }

    public static List<Term> parse(final String str) {
        return new BaseAnalysis(null).parseStr(str);
    }
}
