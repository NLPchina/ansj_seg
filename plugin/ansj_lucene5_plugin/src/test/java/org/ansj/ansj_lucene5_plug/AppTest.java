package org.ansj.ansj_lucene5_plug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) throws IOException {
        Set<String> filter = new HashSet<String>() ;
        
        String stopDicStr = "6\n7" ;  
        
        BufferedReader br = new BufferedReader(new StringReader(stopDicStr)) ;
        String temp = null ;
        while((temp=br.readLine())!=null){
            filter.add(temp) ;
        }
        
        StringReader reader = new StringReader("龙虎胶囊 6 * 7cm") ;
        Tokenizer tokenizer = new AnsjTokenizer(new IndexAnalysis(reader), filter);
        while(tokenizer.incrementToken()){
            CharTermAttribute attribute = tokenizer.getAttribute(CharTermAttribute.class) ;
            System.out.println(attribute);
        }
    }
}
