package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class TermNatureTest {

    @Test
    public void test1() {
        TermNature termNature = new TermNature("a", 1);
        System.out.println(termNature.toString());
        Assert.assertEquals(termNature.toString(), "a/1");
    }
}
