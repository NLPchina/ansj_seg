package org.ansj.app.keyword;

import org.junit.Assert;
import org.junit.Test;

public class KeyWordTest {

    @Test
    public void equals() {
        Keyword keyword = new Keyword("jack", 1.0);
        Keyword keyword1 = new Keyword("jack", 1.0);
        System.out.println(keyword.equals(keyword1));
        Assert.assertTrue(keyword.equals(keyword1));
        System.out.println(keyword.equals("a"));
        Assert.assertFalse(keyword.equals("a"));
    }

    @Test
    public void setName() {
        Keyword keyword = new Keyword("jack", 1.0);
        keyword.setName("jack1");
        System.out.println(keyword.getName());
        Assert.assertEquals(keyword.getName(), "jack1");
    }

    @Test
    public void setScore() {
        Keyword keyword = new Keyword("jack", 1.0);
        keyword.setScore(2.0);
        System.out.println(keyword.getScore());
        Assert.assertTrue(2.0 == keyword.getScore());
    }

    @Test
    public void getFreq() {
        Keyword keyword = new Keyword("jack", 1.0);
        System.out.println(keyword.getFreq());
        Assert.assertTrue(1 == keyword.getScore());
    }
}
