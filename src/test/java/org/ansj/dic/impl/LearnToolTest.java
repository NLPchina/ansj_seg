package org.ansj.dic.impl;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class LearnToolTest {

    @Test
    public void test() throws IOException {
        // 构建一个新词学习的工具类。这个对象。保存了所有分词中出现的新词。出现次数越多。相对权重越大。
        LearnTool learnTool = new LearnTool();

        NlpAnalysis nlpAnalysis = new NlpAnalysis().setLearnTool(learnTool);

        // 进行词语分词。也就是nlp方式分词，这里可以分多篇文章
        nlpAnalysis.parseStr("说过，社交软件也是打着沟通的平台，让无数寂寞男女有了肉体与精神的寄托。");
        nlpAnalysis.parseStr("其实可以打着这个需求点去运作的互联网公司不应只是社交类软件与可穿戴设备，还有携程网，去哪儿网等等，订房订酒店多好的寓意");
        nlpAnalysis.parseStr("张艺谋的卡宴，马明哲的戏");

        // 只取得词性为Nature.NR的新词
        System.out.println(learnTool.getTopTree(10, Nature.NR));
        Assert.assertEquals(learnTool.getTopTree(10, Nature.NR).toString(), "[]");
    }
}
