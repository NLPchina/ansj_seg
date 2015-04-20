package org.ansj.ansj_lucene4_plug;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) throws IOException {
//        Set<String> filter = new HashSet<String>() ;
//        
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/ansj/公共的/stopLibrary.dic"))) ;
//        
//        String temp = null ;
//        
//        while((temp=br.readLine())!=null){
//            filter.add(temp) ;
//        }
//        
//        StringReader reader = new StringReader("龙虎胶囊 6 * 7cm") ;
//        Tokenizer tokenizer = new AnsjTokenizer(new IndexAnalysis(reader), reader, filter, false);
//        while(tokenizer.incrementToken()){
//            CharTermAttribute attribute = tokenizer.getAttribute(CharTermAttribute.class) ;
//            System.out.println(attribute);
//        }
        PrefixQuery pq = new PrefixQuery(new Term("name","中国")) ;
        System.out.println(pq);
    }
}
