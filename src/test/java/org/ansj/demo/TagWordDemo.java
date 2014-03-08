package org.ansj.demo;

import java.util.ArrayList;
import java.util.List;

import org.ansj.app.summary.TagContent;

public class TagWordDemo {
	public static void main(String[] args) {
		TagContent tw = new TagContent("<begin>", "<end>");
		String content = "台湾两岸共同市场基金会代表团   不断推动两岸关";
		List<String> keyWords = new ArrayList<String>();
		keyWords.add("两岸关系");
		keyWords.add("两岸");
		keyWords.add("李克强");
		keyWords.add("博鳌");
		keyWords.add("台湾");
		System.out.println(tw.tagContent(keyWords, content));;
	}
}
