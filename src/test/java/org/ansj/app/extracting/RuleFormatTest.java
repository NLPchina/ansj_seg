package org.ansj.app.extracting;

import org.ansj.app.extracting.exception.RuleFormatException;
import org.junit.Assert;
import org.junit.Test;


public class RuleFormatTest {

    @Test
    public void ruleFormatTest() {
        try {
            setAge(-1);
        } catch (RuleFormatException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Assert.assertEquals(e.getMessage(), "age error");
        }
    }

    private void setAge(int age) throws RuleFormatException {
        if (age < 0) {
            throw new RuleFormatException("age error");
        }
    }
}
