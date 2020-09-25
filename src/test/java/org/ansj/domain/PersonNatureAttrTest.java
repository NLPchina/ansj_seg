package org.ansj.domain;

import org.ansj.exception.LibraryException;
import org.junit.Assert;
import org.junit.Test;


public class PersonNatureAttrTest {

    @Test
    public void test1() {
        try {
            set();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertEquals(e.getMessage(), "person name status err a");
        }
    }

    private void set() throws LibraryException {
        PersonNatureAttr personNatureAttr = new PersonNatureAttr();
        try {
            personNatureAttr.set('a', 1.0f);
        } catch (Exception e) {
            throw new LibraryException(e.getMessage());
        }
    }
}
