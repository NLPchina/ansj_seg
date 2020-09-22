package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class NumNatureAttrTest {

    @Test
    public void test1() {
        NumNatureAttr numNatureAttr = new NumNatureAttr(false, true, "a");
        System.out.println(numNatureAttr.getNature());
        Assert.assertEquals(numNatureAttr.getNature().toString(), "a:2:2");
    }

    @Test
    public void test2() {
        NumNatureAttr numNatureAttr = new NumNatureAttr(true, true, "b");
        numNatureAttr.setQua(false);
        System.out.println(numNatureAttr.isQua());
        Assert.assertFalse(numNatureAttr.isQua());
    }
}
