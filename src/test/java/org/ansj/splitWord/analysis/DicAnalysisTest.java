package org.ansj.splitWord.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.ansj.CorpusTest;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import junit.framework.Assert;

public class DicAnalysisTest  extends CorpusTest {

	@Test
	public void test() throws IOException {
		for (String string : lines) {
			System.out.println(DicAnalysis.parse(string));
		}
	}
	
	@Test
	public void test1(){
		DicLibrary.insert(DicLibrary.DEFAULT, "金水区", "ad", 1000);
		DicLibrary.insert(DicLibrary.DEFAULT, "渝北区", "ad", 1000);
		DicLibrary.insert(DicLibrary.DEFAULT, "金童路", "ad", 1000);
		DicLibrary.insert(DicLibrary.DEFAULT, "奥山", "ad", 1000);
		DicLibrary.insert(DicLibrary.DEFAULT, "来自大", "ab", 1000);
		DicLibrary.insert(DicLibrary.DEFAULT, "自大学", "ab", 2000);
		DicLibrary.insert(DicLibrary.DEFAULT, "网大学", "ab", 1000);

		System.out.println(DicAnalysis.parse("重庆重庆市渝北区金童路奥山别墅162"));
		System.out.println(DicAnalysis.parse("河南省郑州市金水区金水区农科路与文博西路交叉口向东２００米路南"));
		System.out.println(DicAnalysis.parse("来自大学生小说网大学"));

		String newWord = "爸爸去哪儿";
		String nature = "aaaaa";
		String str = "上海电力2012年财务报表如下怎爸爸去哪儿么办";

		//增加新词
		DicLibrary.insert(DicLibrary.DEFAULT, newWord, nature, 1000);
		DicLibrary.insert(DicLibrary.DEFAULT, "上海电力", nature, 1000);

		List<Term> parse = DicAnalysis.parse(str).getTerms();
		HashMap<String, Term> hs = new HashMap<String, Term>();
		for (Term term : parse) {
			hs.put(term.getName(), term);
		}
		Assert.assertTrue(hs.containsKey(newWord));

		Assert.assertEquals(hs.get(newWord).natrue().natureStr, nature);

		Library.insertWord(DicLibrary.get(), new Value("北京卡", "UserDefined", "1000"));

		Assert.assertEquals(DicAnalysis.parse("北京卡机场服务").get(0).getName(), "北京卡");

		//删除词
		DicLibrary.delete(DicLibrary.DEFAULT, newWord);
		parse = DicAnalysis.parse(str).getTerms();
		hs = new HashMap<String, Term>();
		for (Term term : parse) {
			hs.put(term.getName(), term);
		}

		Assert.assertTrue(!hs.containsKey(newWord));
	}

}
