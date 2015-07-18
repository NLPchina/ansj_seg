package org.ansj.test;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.ToAnalysis;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDefinedAnalysisTest {

    @Test
    public void test() {
        final UserDefineLibrary userDefineLibrary = UserDefineLibrary.getInstance();

        String newWord = "爸爸去哪儿";
        String nature = "aaaaa";
        String str = "上海电力2012年财务报表如下怎爸爸去哪儿么办";

        //增加新词
        userDefineLibrary.insertWord(newWord, nature, 1000);

        List<Term> parse = ToAnalysis.parse(str);
        HashMap<String, Term> hs = new HashMap<>();
        for (Term term : parse) {
            hs.put(term.getName(), term);
        }

        assertTrue(hs.containsKey(newWord));

        assertEquals(hs.get(newWord).getNature().natureStr, nature);

        //删除词
        userDefineLibrary.removeWord(newWord);
        parse = ToAnalysis.parse(str);
        hs = new HashMap<>();
        for (Term term : parse) {
            hs.put(term.getName(), term);
        }

        assertTrue(!hs.containsKey(newWord));
    }
}
