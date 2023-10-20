package org.ansj.splitWord.analysis;

import org.ansj.splitWord.impl.GetWordsImpl;
import org.junit.Assert;
import org.junit.Test;

public class GetWordsImplTest {
    @Test
    public void test() {
        GetWordsImpl getWordsImpl = new GetWordsImpl("a");
        System.out.println(getWordsImpl.chars);
        Assert.assertArrayEquals(getWordsImpl.chars, new char[]{'a'});
    }

    @Test
    public void test1() {
        GetWordsImpl getWordsImpl = new GetWordsImpl();
        System.out.println(getWordsImpl.getOffe());
        Assert.assertEquals(getWordsImpl.getOffe(), 0);
    }
}

