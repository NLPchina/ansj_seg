## ansj solr4 plugin

edit schema.xml:
```

    <analyzer type="index">
         <tokenizer class="org.ansj.solr.AnsjTokenizerFactory"  isQuery="false"/>
    </analyzer>
    <analyzer type="query">
        <tokenizer class="org.ansj.solr.AnsjTokenizerFactory"/>
    </analyzer>
    
```