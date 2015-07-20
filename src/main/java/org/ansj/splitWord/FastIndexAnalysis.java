package org.ansj.splitWord;

import lombok.SneakyThrows;
import org.ansj.Term;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
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

    @SneakyThrows
    public static void main(final String[] args) {
        BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/temp/test.txt", "utf-8");

        System.out.println(FastIndexAnalysis.parse("河北省"));
        long start = System.currentTimeMillis();

        FastIndexAnalysis fia = new FastIndexAnalysis(reader);
        long length = 0;

        Term temp;
        while ((temp = fia.next()) != null) {
            length += temp.getName().length();
        }

        System.out.println(length * 1000L / (System.currentTimeMillis() - start));

        System.out.println(System.currentTimeMillis() - start);

        System.out.println(length);
    }
}
