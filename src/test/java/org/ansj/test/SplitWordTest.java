package org.ansj.test;

import org.ansj.util.MyStaticValue;
import org.junit.Test;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;

import static org.ansj.util.MyStaticValue.TAB;

public class SplitWordTest {

    @Test
    public void cohesionTest() throws IOException {
        long start = System.currentTimeMillis();
        try (final BufferedReader reader = IOUtil.getReader(MyStaticValue.userLibrary, IOUtil.UTF8)) {
            String temp;
            while ((temp = reader.readLine()) != null) {
                String word = temp.split(TAB)[0];
                double value = MyStaticValue.getCRFSplitWord().cohesion(word);
                System.out.println(word + TAB + value);
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
