package org.ansj.ansj_lucene4_plug;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.lucene4.AnsjIndexAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import love.cq.util.IOUtil;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) throws IOException {
        Set<String> filter = new HashSet<String>() ;
        
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/ansj/公共的/stopLibrary.dic"))) ;
        
        String temp = null ;
        
        while((temp=br.readLine())!=null){
            filter.add(temp) ;
        }
        
        StringReader reader = new StringReader("龙虎胶囊 6 * 7cm") ;
        Tokenizer tokenizer = new AnsjTokenizer(new IndexAnalysis(reader), reader, filter, false);
        while(tokenizer.incrementToken()){
            CharTermAttribute attribute = tokenizer.getAttribute(CharTermAttribute.class) ;
            System.out.println(attribute);
        }
    }
}
