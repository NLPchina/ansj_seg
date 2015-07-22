package org.ansj.demo;

import org.ansj.keyword.Keyword;
import org.ansj.summary.TagContent;

import java.util.ArrayList;
import java.util.List;

public class TagWordDemo {

    public static void main(final String[] args) {
        TagContent tw = new TagContent("<begin>", "<end>");
        String content = "台湾两岸共同市场基金会代表团   不断推动两岸关";
        List<Keyword> keyWords = new ArrayList<Keyword>();
        keyWords.add(new Keyword("两岸关系", 1.0));
        keyWords.add(new Keyword("两岸", 1.0));
        keyWords.add(new Keyword("李克强", 1.0));
        keyWords.add(new Keyword("博鳌", 1.0));
        System.out.println(tw.tagContent(keyWords, content));
    }
}
