package org.ansj.library;

import org.ansj.Nature;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.ansj.AnsjUtils.classpathResource;
import static org.ansj.AnsjUtils.rawLines;

public class NatureLibraryTest {

    private NatureLibrary natureLibrary;

    @Before
    public void setUp() {
        this.natureLibrary = new NatureLibrary(
                rawLines(classpathResource("nature/nature.map")),// 词性表
                rawLines(classpathResource("nature/nature.table")) // 词性关联表
        );
    }

    @Test
    public void testNatureLibrary() {
        final Map<String, Nature> natureMap = this.natureLibrary.natureMap;
        final int[][] natureTable = this.natureLibrary.natureTable;
        //System.out.println(natureTable[natureMap.get("nr").natureIndex][natureMap.get("mq").natureIndex]);
    }
}
