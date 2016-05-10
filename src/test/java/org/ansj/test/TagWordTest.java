package org.ansj.test;

import java.util.ArrayList;
import java.util.List;

import org.ansj.app.keyword.Keyword;
import org.ansj.app.summary.TagContent;
import org.junit.Test;

public class TagWordTest {
	@Test
	public void test(){
		TagContent tw = new TagContent("<em>", "</em>");
		String content = "abc123中国人";
		List<Keyword> keywords = new ArrayList<Keyword>();
		keywords.add(new Keyword("中国人民", 1.0));
		keywords.add(new Keyword("中国", 1.0));
		keywords.add(new Keyword("abc", 1.0));
		keywords.add(new Keyword("abc12", 1.0));
		System.out.println(tw.tagContent(keywords, content));
	}
}
