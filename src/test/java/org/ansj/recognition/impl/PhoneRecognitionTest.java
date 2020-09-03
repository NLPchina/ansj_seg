package org.ansj.recognition.impl;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;

public class PhoneRecognitionTest {

    private String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

    @Test
    public void test() {
        String[] tests = new String[]{
                "13810280881", "15810280881", "18810280881", "17810280881", "14710280881"
        };
        for (int i = 0; i < tests.length; i++) {
            for (Term term : ToAnalysis.parse(tests[i]).recognition(new PhoneRecognition()).getTerms()) {
                String str1 = term + "";
                boolean matches = str1.split("/")[0].matches(regExp);
                Assert.assertTrue(matches);
                System.out.println(str1);
            }
        }
    }

    @Test
    public void test1() {
        String str = "11810280881";
        for (Term term : ToAnalysis.parse(str).recognition(new PhoneRecognition()).getTerms()) {
            String str1 = term + "";
            boolean matches = str1.split("/")[0].matches(regExp);
            Assert.assertFalse(matches);
            System.out.print(str1);
        }
    }
}
