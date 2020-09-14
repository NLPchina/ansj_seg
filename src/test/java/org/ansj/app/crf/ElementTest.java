package org.ansj.app.crf;

import org.ansj.app.crf.pojo.Element;
import org.junit.Assert;
import org.junit.Test;

public class ElementTest {

    @Test
    public void elementTest() {
        Character name = 'A';
        int tag = 1;
        Element element = new Element(name, tag);
        System.out.println(element);
        element.updateNature("a");
        System.out.println(element.getName());
        System.out.println(element.nameStr());
        Assert.assertEquals(element.getName(), 'A');
        Assert.assertEquals(element.nameStr(), "A");
    }

    @Test
    public void nameStrTest1() {
        Character name = 130;
        Element element = new Element(name);
        System.out.println(element.nameStr());
        Assert.assertEquals(element.nameStr(), "num0");
    }

    @Test
    public void nameStrTest2() {
        Character name = 140;
        Element element = new Element(name);
        System.out.println(element.nameStr());
        Assert.assertEquals(element.nameStr(), "en0");
    }
}
