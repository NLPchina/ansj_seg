package org.ansj.app.crf;

import org.ansj.app.crf.pojo.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConfigTest {

    @Test
    public void wordAlertTest() {
        List<Element> wordAlert = Config.wordAlert("超过1亿");
        System.out.println(wordAlert);
        Assert.assertEquals(wordAlert.toString(), "[超/1/-1, 过/1/-1, /1/-1, 亿/1/-1]");

    }

    @Test
    public void setTemplateTest() {
        int[][] template = {{-2}, {-1}, {0}, {1}, {2}};
        Config config = new Config(template);
        int[][] template1 = {{-2}};
        config.setTemplate(template1);
        int[][] template2 = config.getTemplate();
        Assert.assertArrayEquals(template1, template2);
    }

    @Test
    public void makeToElementList() {
        int[][] template = {{-2}, {-1}, {0}, {1}, {2}};
        Config config = new Config(template);
        String temp = "超过1亿";
        List<Element> wordAlert = config.makeToElementList(temp);
        System.out.println(wordAlert.toString());
        Assert.assertEquals(wordAlert.toString(), "[超/1/-1, 过/1/-1, /1/-1, 亿/1/-1]");
    }

    @Test
    public void makeToElementList1() {
        String temp = "超过1亿";
        String splitStr = "1";
        List<Element> wordAlert = Config.makeToElementList(temp, splitStr);
        System.out.println(wordAlert.toString());
        Assert.assertEquals(wordAlert.toString(), "[超/1/1, 过/1/3, 亿/1/0]");
    }

    @Test
    public void getTagIfOutArr() {
        int[][] template = {{-2}, {-1}, {0}, {1}, {2}};
        Config config = new Config(template);
        List<Element> list = Config.wordAlert("超过1亿");
        char wordAlert = config.getTagIfOutArr(list, 0);
        System.out.println(wordAlert);
        Assert.assertEquals(wordAlert, 65535);
    }

    @Test
    public void getTagIfOutArr1() {
        int[][] template = {{-2}, {-1}, {0}, {1}, {2}};
        Config config = new Config(template);
        List<Element> list = Config.wordAlert("超过1亿");
        char wordAlert = config.getTagIfOutArr(list, -1);
        System.out.println(wordAlert);
        Assert.assertEquals(wordAlert, 0);
    }

    @Test
    public void getTagName0() {
        int tag = 0;
        char tagName = Config.getTagName(tag);
        System.out.println(tagName);
        Assert.assertEquals(tagName, 'S');
    }

    @Test
    public void getTagName1() {
        int tag = 1;
        char tagName = Config.getTagName(tag);
        System.out.println(tagName);
        Assert.assertEquals(tagName, 'B');
    }

    @Test
    public void getTagName2() {
        int tag = 2;
        char tagName = Config.getTagName(tag);
        System.out.println(tagName);
        Assert.assertEquals(tagName, 'M');
    }

    @Test
    public void getTagName3() {
        int tag = 3;
        char tagName = Config.getTagName(tag);
        System.out.println(tagName);
        Assert.assertEquals(tagName, 'E');
    }

    @Test
    public void getTagName4() {
        int tag = 4;
        char tagName = Config.getTagName(tag);
        System.out.println(tagName);
        Assert.assertEquals(tagName, '?');
    }
}

