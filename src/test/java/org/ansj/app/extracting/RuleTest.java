package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.Rule;
import org.ansj.app.extracting.domain.Token;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;


public class RuleTest {

    @Test
    public void test() {
        String ruleStr = "a";
        List<Token> tokens = null;
        Map<String, int[]> groups = null;
        Map<String, String> attr = null;
        double weight = 1.0;
        Rule rule = new Rule(ruleStr, tokens, groups, attr, weight);
        rule.setTokens(null);
        rule.setGroups(null);
        rule.setWeight(2.0);
        Assert.assertNull(rule.getTokens());
        Assert.assertNull(rule.getGroups());
        Assert.assertTrue(rule.getWeight() == 2.0);
        Assert.assertSame(rule.getRuleStr(), "a");
    }
}
