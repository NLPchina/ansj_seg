package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class AnsjItemTest {

    @Test
    public void test1() {
        AnsjItem ansjItem = new AnsjItem();
        String[] split = {"a", "b"};
        ansjItem.init(split);
        System.out.println(ansjItem.param);
        Assert.assertEquals(ansjItem.param, "b");
    }

    @Test
    public void test2() {
        AnsjItem ansjItem = new AnsjItem();
        System.out.println(ansjItem.toText());
        Assert.assertEquals(ansjItem.toText(), "0	null	65536	0	0	null");
    }
}
