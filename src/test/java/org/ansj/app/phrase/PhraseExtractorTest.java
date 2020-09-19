package org.ansj.app.phrase;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PhraseExtractorTest {

    @Test
    public void test1() {
        PhraseExtractor phraseExtractor = new PhraseExtractor();
        List<Map.Entry<String, Occurrence>> nbest = phraseExtractor.nbest(2);
        Assert.assertTrue(nbest.size() == 0);
    }
}
