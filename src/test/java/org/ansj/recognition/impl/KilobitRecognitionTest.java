package org.ansj.recognition.impl;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class KilobitRecognitionTest {
    private StringBuilder stringBuilder = new StringBuilder();

    @Test
    public void test() {
        String[] tests = new String[]{
                "12,345.60"
        };
        for (String str : tests) {
            for (Term term : ToAnalysis.parse(str).recognition(new KilobitRecognition()).getTerms()) {
                stringBuilder.append(term + " ");
            }
            System.out.println(stringBuilder.toString());
            Assert.assertEquals(stringBuilder.toString(), "12/m ,/w 345.60/m ");
        }
    }

    @Test
    public void test1() {
        String[] tests = new String[]{
                "12,345"
        };
        for (String str : tests) {
            for (Term term : ToAnalysis.parse(str).recognition(new KilobitRecognition()).getTerms()) {
                stringBuilder.append(term + " ");
            }
            System.out.println(stringBuilder.toString());
            Assert.assertEquals(stringBuilder.toString(), "12/m ,/w 345/m ");
        }
    }
}
