package org.ansj.test;

import org.ansj.Term;
import org.ansj.splitWord.NlpAnalysis;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.ansj.AnsjContext.TAB;

public class TestSigHan {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = IOUtil.getReader("/home/ansj/src/icwb2-data/testing/msr_test.utf8", "utf-8");
        String temp = null;
        FileOutputStream fos = new FileOutputStream(new File("/home/ansj/src/icwb2-data/test_segmentation.utf8"));
        while ((temp = reader.readLine()) != null) {
            List<Term> parse = NlpAnalysis.nlpParse(temp);
            StringBuilder sb = new StringBuilder();
            for (Term term : parse) {
                sb.append(term.getName() + TAB);
            }
            fos.write(sb.toString().trim().getBytes());
            fos.write("\n".getBytes());
        }

        fos.flush();
        fos.close();
    }
}
