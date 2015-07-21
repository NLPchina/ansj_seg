package org.ansj.splitWord;

import lombok.SneakyThrows;
import org.ansj.Term;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;

public class FastIndexAnalysisDemo {

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
