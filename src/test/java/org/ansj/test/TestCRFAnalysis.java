package org.ansj.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.CRFAnalysis;

public class TestCRFAnalysis {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = IOUtil.getReader(
            "/home/ansj/文档/data/corpus/sport/sports.sohu.com.txt", "utf-8");

        String temp = null;
        List<Term> parse = CRFAnalysis.parse("测试123孙建");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
            "/home/ansj/文档/data/corpus/sport/sports.sohu.com.txt.result")));
        LearnTool learn = new LearnTool();
        int len = 0 ;
        long start = System.currentTimeMillis();
        while ((temp = reader.readLine()) != null) {
            len += temp.length() ;
            parse = CRFAnalysis.parse(temp, learn);
            bw.write(parse.toString());
            bw.write("\n");
        }
        System.out.println(len);
        System.out.println(System.currentTimeMillis() - start);
        bw.flush();
        bw.close();
    }
}
