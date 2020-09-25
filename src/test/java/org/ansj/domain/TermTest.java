package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class TermTest {

    @Test
    public void test1() {
        Term term = new Term("a", 1, "b", 2);
        term.setOffe(3);
        System.out.println(term.getOffe());
        Assert.assertEquals(term.getOffe(), 3);
    }

    @Test
    public void test2() {
        Term term = new Term("a1", 11, "b1", 22);
        term.setRealName("c1");
        Term term1 = term.merage(term);
        System.out.println(term1.getRealName());
        Assert.assertEquals(term1.getRealName(), "c1c1");
    }

    @Test
    public void test3() {
        Term term = new Term("a", 1, "b", 2);
        term.updateOffe(4);
        System.out.println(term.getOffe());
        Assert.assertEquals(term.getOffe(), 5);
    }
}
