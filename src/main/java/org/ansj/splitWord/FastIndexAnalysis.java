package org.ansj.splitWord;

import org.ansj.Term;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class FastIndexAnalysis extends Analysis {

    public FastIndexAnalysis(final Reader br) {
        super(null);
        if (br != null) {
            super.resetContent(br);
        }
    }

    public FastIndexAnalysis() {
        this(null);
    }

    public static List<Term> parse(final String str) {
        return new FastIndexAnalysis().parseStr(str);
    }

    @Override
    protected List<Term> getResult(final Graph graph) {
        final List<Term> result = new LinkedList<>();
        final int length = graph.terms.length - 1;
        Term term;
        for (int i = 0; i < length; i++) {
            if ((term = graph.terms[i]) != null) {
                result.add(term);
                while ((term = term.getNext()) != null) {
                    result.add(term);
                }
            }
        }

        return result;
    }
}
