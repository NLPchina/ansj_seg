package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.Token;
import org.junit.Assert;
import org.junit.Test;


public class TokenTest {

    @Test
    public void test() {
        int index = 1;
        String termsStr = "abcd";
        Token token = new Token(index, termsStr);
        token.setIndex(2);
        token.setTerms(null);
        token.setRegexs(null);
        token.setRange(null);
        System.out.println(token.toString());
        Assert.assertTrue(token.getIndex() == 2);
        Assert.assertNull(token.getTerms());
        Assert.assertTrue(token.getRegexs().size() == 0);
        Assert.assertNull(token.getRange());
        Assert.assertNull(token.getPrev());
        Assert.assertEquals(token.toString(), "index=2,terms=null,regexs=null,range=null");
    }
}
