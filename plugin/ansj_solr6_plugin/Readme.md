## ansj solr6 plugin

edit managed-schema:
```

    <analyzer type="index">
         <tokenizer class="org.ansj.solr.AnsjTokenizerFactory"  isQuery="false"/>
    </analyzer>
    <analyzer type="query">
        <tokenizer class="org.ansj.solr.AnsjTokenizerFactory"/>
    </analyzer>
    
```