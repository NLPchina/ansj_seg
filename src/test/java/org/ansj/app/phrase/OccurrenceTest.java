package org.ansj.app.phrase;

import org.ansj.domain.Term;
import org.junit.Assert;
import org.junit.Test;
import org.nlpcn.commons.lang.util.MapCount;

import java.util.ArrayList;
import java.util.List;

public class OccurrenceTest {
    List<Term> terms = new ArrayList<>();
    Occurrence occurrence = new Occurrence(terms);

    @Test
    public void test() {
        occurrence.addLeftTerm("a");
        MapCount<String> leftTerms = occurrence.getLeftTerms();
        System.out.println("leftTerms.size()=" + leftTerms.size());
        Assert.assertTrue(leftTerms.size() == 1);
        occurrence.addRightTerm("b");
        MapCount<String> rightTerms = occurrence.getRightTerms();
        System.out.println("rightTerms.size()=" + rightTerms.size());
        Assert.assertTrue(rightTerms.size() == 1);
    }

    @Test
    public void test1() {
        List<Term> terms1 = occurrence.getTerms();
        System.out.println("terms1.size()=" + terms1.size());
        Assert.assertTrue(terms1.size() == 0);
        occurrence.increaseFrequency();
        System.out.println("occurrence.getFrequency()=" + occurrence.getFrequency());
        Assert.assertTrue(occurrence.getFrequency() == 1);
    }

    @Test
    public void test2() {
        occurrence.setPmi(1.0);
        System.out.println("occurrence.getPmi()=" + occurrence.getPmi());
        Assert.assertTrue(occurrence.getPmi() == 1.0);
        occurrence.setLeftEntropy(2.0);
        System.out.println("occurrence.getLeftEntropy()=" + occurrence.getLeftEntropy());
        Assert.assertTrue(occurrence.getLeftEntropy() == 2.0);
    }

    @Test
    public void test3() {
        occurrence.setRightEntropy(3.0);
        System.out.println("occurrence.getRightEntropy()=" + occurrence.getRightEntropy());
        Assert.assertTrue(occurrence.getRightEntropy() == 3.0);
        occurrence.setScore(4.0);
        System.out.println("occurrence.getScore()=" + occurrence.getScore());
        Assert.assertTrue(occurrence.getScore() == 4.0);
    }
}
