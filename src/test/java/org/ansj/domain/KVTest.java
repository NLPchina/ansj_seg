package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class KVTest {

    @Test
    public void test1() {
        KV<String, String> kv = KV.with("a", "b");
        kv.setK("a1");
        System.out.println(kv.getK());
        Assert.assertEquals(kv.getK(), "a1");
    }
}
