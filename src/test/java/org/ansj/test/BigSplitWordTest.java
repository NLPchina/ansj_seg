package org.ansj.test;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.ansj.util.AnsjContext.CONTEXT;
import static org.junit.Assert.assertTrue;

public class BigSplitWordTest {

    @Test
    public void CRFSplitTest() {
        final List<String> cut = CONTEXT().getCrfSplitWord().cut("协会主席亚拉·巴洛斯说他们是在1990年开始寻找野生金刚鹦鹉的");
        final Set<String> words = new HashSet<>(cut);
        assertTrue(words.contains("亚拉·巴洛斯"));
    }
}
