package org.ansj.test;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.ToAnalysis;
import org.junit.Test;

import java.util.HashMap;

import static org.ansj.util.AnsjContext.CONTEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDefinedAnalysisTest {

    @Test
    public void test() {
        final UserDefineLibrary userDefineLibrary = CONTEXT().getUserDefineLibrary();

        final String newWord = "爸爸去哪儿";
        final String nature = "aaaaa";
        final String str = "上海电力2012年财务报表如下怎爸爸去哪儿么办";

        //增加新词
        userDefineLibrary.insertWord(newWord, nature, 1000);

        final HashMap<String, Term> map0 = new HashMap<>();
        ToAnalysis.parse(str).forEach(term -> map0.put(term.getName(), term));

        assertTrue(map0.containsKey(newWord));
        assertEquals(map0.get(newWord).getNature().natureStr, nature);

        //删除词
        final HashMap<String, Term> map1 = new HashMap<>();
        userDefineLibrary.removeWord(newWord);
        ToAnalysis.parse(str).forEach(term -> map1.put(term.getName(), term));
        assertTrue(!map1.containsKey(newWord));
    }
}
