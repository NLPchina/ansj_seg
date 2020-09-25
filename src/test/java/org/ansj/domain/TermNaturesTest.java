package org.ansj.domain;

import org.junit.Assert;
import org.junit.Test;


public class TermNaturesTest {

    @Test
    public void test1() {
        TermNature termNature = new TermNature("a", 1);
        TermNatures termNatures = new TermNatures(termNature);
        PersonNatureAttr personNatureAttr = new PersonNatureAttr();
        termNatures.setPersonNatureAttr(personNatureAttr);
        PersonNatureAttr personAttr = termNatures.personAttr;
        System.out.println(PersonNatureAttr.NULL);
        System.out.println(personAttr);
        Assert.assertNotSame(personAttr, PersonNatureAttr.NULL);
    }
}
