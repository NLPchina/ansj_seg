#Ansj In Solr
=============
//FIXME: 临时占位


感谢姚维风同学为我提供了...solr接口..下面是他的完整的邮件...我没做任何修改..我意思是出了问题问他去哈呵呵...
http://weibo.com/u/2131579474?topnav=1&wvr=5&topsug=1

为了与solr集成，我们在同类型分词系统mmseg4j提供的TokenizerFactory之上针对ansj-seg进行的了改造。由于看到github有人提出是否能针对solr进行支持，所以我们将我们的solr实现发给你，仅作参考。solr版本：solr-4.0.0。该版本在实际solr-cloud中进行过测试，性能优越！再次感谢！


```
package org.ansj.solr;

import java.io.IOException;
import java.io.Reader;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class ANSJTokenizer extends Tokenizer {
	Analysis udf;
	 private CharTermAttribute termAtt;
     private OffsetAttribute offsetAtt;
     private TypeAttribute typeAtt;
	protected ANSJTokenizer(Reader input) {
		super(input);		
		termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
		offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
		typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
	}	
	
	@Override
	public void reset() throws IOException {
		super.reset();
		udf = new ToAnalysis(input);
	}
	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		Term term = udf.next();
		
        if(term != null) {
                termAtt.copyBuffer(term.getName().toCharArray(), 0, term.getName().length());
                offsetAtt.setOffset(term.getOffe(), term.getTo().getOffe());
                typeAtt.setType("word");
                return true;
        } else {
                end();
                return false;
        }
	}

}

```


```
/**
 * 
 */
package org.ansj.solr;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenlb
 * @author WEIFENG.YAO
 *
 */
public class ANSJTokenizerFactory extends TokenizerFactory implements
		ResourceLoaderAware {
    static final Logger logger = LoggerFactory.getLogger(ANSJTokenizerFactory.class);

    private ThreadLocal<ANSJTokenizer> tokenizerLocal = new ThreadLocal<ANSJTokenizer>();

	public void inform(ResourceLoader loader) throws IOException {
		
	}

	
	@Override
	public Tokenizer create(Reader input) {
		ANSJTokenizer tokenizer = tokenizerLocal.get();		
		if(tokenizer == null) {
            tokenizer = newTokenizer(input);
	    } else {
	            try {
	            		tokenizer.setReader(input);
	                    tokenizer.reset();
	            } catch (IOException e) {
	                    tokenizer = newTokenizer(input);
	            }
	    }
	
	    return tokenizer;
	}

	
	  private ANSJTokenizer newTokenizer(Reader input) {
		  ANSJTokenizer tokenizer = new ANSJTokenizer(input);
          tokenizerLocal.set(tokenizer);
          return tokenizer;
  }
}
```

```
1、编译代码需要依赖solr4.0.0，maven下  
<dependency>
	    <groupId>org.apache.solr</groupId>
	    <artifactId>solr-core</artifactId>
	    <version>4.0.0</version>
</dependency>


2、在solr的schema.xml 中添加
   
  <fieldType name="text_cn" class="solr.TextField" positionIncrementGap="100">
      <analyzer type="index">
		<tokenizer class="org.ansj.solr.ANSJTokenizerFactory"/>
		<filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" enablePositionIncrements="true" />
         <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
      <analyzer type="query">
		<tokenizer class="org.ansj.solr.ANSJTokenizerFactory"/>
		<filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" enablePositionIncrements="true" />
        <filter class="solr.LowerCaseFilterFactory"/>
      </analyzer>
    </fieldType>

StopFilterFactory中的stopwords.txt可使用ansj-seg下的library/stopwords.txt 



 <field name="your own field" type="text_cn" indexed="true" stored="false" required="false"/>
```