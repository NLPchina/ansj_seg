package org.ansj.app.summary;

import org.ansj.app.summary.pojo.Summary;
import org.junit.Assert;
import org.junit.Test;

public class SummaryComputerTest {
    SummaryComputer summaryComputer = new SummaryComputer("a", "b");

    @Test
    public void test1() {
        SummaryComputer summaryComputer1 = new SummaryComputer(1, "c", "d");
        Assert.assertNotSame(summaryComputer, summaryComputer1);
    }

    @Test
    public void test2() {
        Summary summary = summaryComputer.toSummary();
        System.out.println(summary.getSummary());
        Assert.assertEquals(summary.getSummary(), "b");
    }

    @Test
    public void test3() {
        Summary summary1 = summaryComputer.toSummary("e");
        System.out.println(summary1.getSummary());
        Assert.assertEquals(summary1.getSummary(), "b");
    }
}
