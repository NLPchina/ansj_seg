package org.ansj.test;

import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.splitWord.analysis.UserDefineAnalysis;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

public class UserDefinedAnalysisTest {

	@Test
	public void test() {
		String newWord = "爸爸去哪儿";
		String nature = "aaaaa";
		String str = "上海电力2012年财务报表如下怎爸爸去哪儿么办";
		
		//增加新词
		UserDefineLibrary.insertWord(newWord, nature, 1000);
		UserDefineLibrary.insertWord("上海电力", nature, 1000);
		
		List<Term> parse = UserDefineAnalysis.parse(str);
		HashMap<String, Term> hs = new HashMap<String, Term>();
		for (Term term : parse) {
			hs.put(term.getName(), term);
		}
		Assert.assertTrue(hs.containsKey(newWord));

		Assert.assertEquals(hs.get(newWord).natrue().natureStr, nature);
		
		
		Library.insertWord(UserDefineLibrary.FOREST, new Value("北京卡", "UserDefined", "1000"));
	    
		Assert.assertEquals(UserDefineAnalysis.parse("北京卡机场服务").get(0).getName(), "北京卡");

		//删除词
		UserDefineLibrary.removeWord(newWord);
		parse = UserDefineAnalysis.parse(str);
		hs = new HashMap<String, Term>();
		for (Term term : parse) {
			hs.put(term.getName(), term);
		}

		Assert.assertTrue(!hs.containsKey(newWord));

	}
}
