package org.ansj.app.keyword;

import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class KeyWordComputerTest {

    @Test
    public void test() {
		String content = "北京英富森软件股份有限公司是在北京市海淀区注册的高新技术企业、双软企业。“信息中国”（information china简称“infcn” ）是“英富森”的核心目标与战略。英富森公司的成立依托于凌云实验室的部分成果和理念，主要以信息管理与信息服务、知识管理与知识服务为基本方向，侧重于信息的整合、组织、发现和利用。通过先进的信息技术和服务理念，帮助行业客户建立企业级信息服务与知识服务平台，实现客户的企业级信息与知识的应用与发现。 公司来源于信息行业，依托于高校和科研院所，服务于行业客户。英富森凝聚了一支专业、高效、快乐、融洽的优秀团队，锻造出了一支服务型、管理型、创新型与开拓型的团队。" ;
        KeyWordComputer kwc = new KeyWordComputer(3,new NlpAnalysis());
        System.out.println(kwc.computeArticleTfidf(content));
    }

    @Test
    public void test1() {
        KeyWordComputer keyWordComputer = new KeyWordComputer();
        keyWordComputer.setAnalysisType(new NlpAnalysis());
        keyWordComputer = new KeyWordComputer(1);
        List<Keyword> list = keyWordComputer.computeArticleTfidf("", "");
        System.out.println(list);
        Assert.assertTrue(list.size() == 0);
    }
}
