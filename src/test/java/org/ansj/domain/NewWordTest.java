package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class NewWordTest {

    @Test
    public void test1() {
        NewWord newWord = new NewWord("a", Nature.NW, 1.0);
        System.out.println(newWord.getName());
        Assert.assertEquals(newWord.getName(), "a");
    }

    @Test
    public void test2() {
        NewWord newWord = new NewWord("a", Nature.NW);
        newWord.setName("a1");
        System.out.println(newWord.getName());
        Assert.assertEquals(newWord.getName(), "a1");
    }

    @Test
    public void test3() {
        NewWord newWord = new NewWord("a", Nature.NW);
        newWord.setNature(Nature.NULL);
        System.out.println(newWord.getNature().toString());
        Assert.assertEquals(newWord.getNature().toString(), "null:-1:-1");
    }

    @Test
    public void test4() {
        NewWord newWord = new NewWord("a", Nature.NW);
        newWord.update(Nature.NULL, 1);
        System.out.println(newWord.getNature().toString());
        Assert.assertEquals(newWord.getNature().toString(), "null:-1:-1");
    }

    @Test
    public void test5() {
        NewWord newWord = new NewWord("a", Nature.NW);
        System.out.println(newWord.toString());
        Assert.assertEquals(newWord.toString(), "a	0.0	nw");
    }
}
