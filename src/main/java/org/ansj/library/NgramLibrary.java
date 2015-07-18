package org.ansj.library;

import org.ansj.domain.AnsjItem;
import org.ansj.domain.Term;
import org.ansj.util.MyStaticValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.ansj.domain.AnsjItem.*;
import static org.ansj.util.MyStaticValue.TAB;

/**
 * 两个词之间的关联
 *
 * @author ansj
 */
public class NgramLibrary {

    /**
     * 词与词之间的关联表数据
     */
    public NgramLibrary(final List<String> bigramdict, final DATDictionary datDictionary) {
        long start = System.currentTimeMillis();
        bigramdict.stream().filter(StringUtils::isNotBlank).forEach(line -> {
            final String[] split = line.split(TAB);
            final int freq = parseInt(split[1]);
            final String[] strs = split[0].split("@");

            final AnsjItem item0 = datDictionary.getItem(strs[0]);
            final AnsjItem item1 = datDictionary.getItem(strs[1]);
            final AnsjItem fromItem = item0 == NULL_ITEM && strs[0].contains("#") ? BEGIN_ITEM : item0;
            final AnsjItem toItem = item1 == NULL_ITEM && strs[1].contains("#") ? END_ITEM : item1;

            if (fromItem != NULL_ITEM && toItem != NULL_ITEM) {
                if (fromItem.bigramEntryMap == null) {
                    fromItem.bigramEntryMap = new HashMap<>();
                }
                fromItem.bigramEntryMap.put(toItem.getIndex(), freq);
            }
        });
        MyStaticValue.LIBRARYLOG.info("init ngram ok use time :" + (System.currentTimeMillis() - start));
    }

    /**
     * 查找两个词与词之间的频率
     */
    public int getTwoWordFreq(final Term from, final Term to) {
        if (from.getItem().bigramEntryMap == null) {
            return 0;
        }
        final Integer freq = from.getItem().bigramEntryMap.get(to.getItem().getIndex());
        return freq != null ? freq : 0;
    }
}
