package org.ansj.app.crf;

import org.ansj.app.crf.pojo.Element;
import org.junit.Test;

import java.util.List;

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
    }

    @Test
    public void nameStrTest1() {
        Character name = 130;
        Element element = new Element(name);
        System.out.println(element.nameStr());
    }

    @Test
    public void nameStrTest2() {
        Character name = 140;
        Element element = new Element(name);
        System.out.println(element.nameStr());
    }
}
