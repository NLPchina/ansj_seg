package org.ansj.test;

import org.junit.Test;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;

import static org.ansj.AnsjContext.CONTEXT;
import static org.ansj.AnsjContext.TAB;

public class SplitWordTest {

    @Test
    public void cohesionTest() throws IOException {
        long start = System.currentTimeMillis();
        try (final BufferedReader reader = IOUtil.getReader(CONTEXT().userLibraryLocation, IOUtil.UTF8)) {
            String temp;
            while ((temp = reader.readLine()) != null) {
                String word = temp.split(TAB)[0];
                double value = CONTEXT().getCrfSplitWord().cohesion(word);
                System.out.println(word + TAB + value);
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
