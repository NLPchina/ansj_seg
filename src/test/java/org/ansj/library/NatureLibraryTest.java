package org.ansj.library;

import org.ansj.domain.Nature;
import org.ansj.util.AnsjUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class NatureLibraryTest {

    private NatureLibrary natureLibrary;

    @Before
    public void setUp() {
        this.natureLibrary = new NatureLibrary(
                AnsjUtils.rawLinesFromClasspath("nature/nature.map"), // 词性表
                AnsjUtils.rawLinesFromClasspath("nature/nature.table") // 词性关联表
        );
    }

    @Test
    public void testNatureLibrary() {
        final Map<String, Nature> natureMap = this.natureLibrary.natureMap;
        final int[][] natureTable = this.natureLibrary.natureTable;
        //System.out.println(natureTable[natureMap.get("nr").natureIndex][natureMap.get("mq").natureIndex]);
    }
}
