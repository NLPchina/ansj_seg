package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.Rule;
import org.ansj.app.extracting.domain.Token;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExtractingTaskTest {

    @Test
    public void getIndex() {
        List<Token> tokens = new ArrayList<>();
        Rule rule = new Rule("a", tokens, null, null, 1.0);
        ExtractingTask extractingTask = new ExtractingTask(null, rule, 1, null);
        System.out.println(extractingTask.getIndex());
        Assert.assertEquals(extractingTask.getIndex(), 1);
    }
}
