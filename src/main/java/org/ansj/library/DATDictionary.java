package org.ansj.library;

import org.ansj.domain.AnsjItem;
import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.dat.Item;

import java.util.Map.Entry;

import static org.ansj.util.MyStaticValue.PERSON_ATTR_LIBRARY;

public class DATDictionary {

    /**
     * 核心词典
     */
    private final DoubleArrayTire trie;
    /**
     * 数组长度
     */
    public int arrayLength;
    /**
     * 所有在词典中出现的词,并且承担简繁体转换的任务.
     */
    private final char[] inSystem;

    public DATDictionary(final DoubleArrayTire coreDic) {
        this.inSystem = new char[65536];
        this.trie = init(coreDic);
        this.arrayLength = trie.arrayLength;
    }

    /**
     * 加载词典
     */
    private DoubleArrayTire init(final DoubleArrayTire coreDic) {
        final long start = System.currentTimeMillis();

        personNameFull(coreDic);

        /**
         * 记录词典中的词语，并且清除部分数据
         */
        for (final Item item : coreDic.getDAT()) {
            if (item != null && item.getName() != null) {
                if (item.getStatus() < 4) {
                    for (int i = 0; i < item.getName().length(); i++) {
                        this.inSystem[item.getName().charAt(i)] = item.getName().charAt(i);
                    }
                }
                if (item.getStatus() < 2) {
                    item.setName(null);
                }
            }
        }
        this.inSystem['％'] = '%';// 特殊字符标准化
        MyStaticValue.LIBRARYLOG.info("init core library ok use time :" + (System.currentTimeMillis() - start));
        return coreDic;
    }

    /**
     * 人名识别必备的
     */
    private void personNameFull(final DoubleArrayTire dat) {
        // 人名词性补录
        final char c = 0;
        for (Entry<String, PersonNatureAttr> entry : PERSON_ATTR_LIBRARY.getPersonMap().entrySet()) {
            final String temp = entry.getKey();

            AnsjItem item;
            if (temp.length() == 1 && dat.getDAT()[temp.charAt(0)] == null) {
                item = new AnsjItem();
                item.setBase(c);
                item.setCheck(-1);
                item.setStatus((byte) 3);
                item.setName(temp);
                dat.getDAT()[temp.charAt(0)] = item;
            } else {
                item = dat.getItem(temp);
            }
            if (item != null) {
                item.termNatures = item.termNatures != null ?
                        item.termNatures.withPersonAttr(entry.getValue()) :
                        new TermNatures(TermNature.NR).withPersonAttr(entry.getValue());
            }
        }
    }

    public boolean inSystem(final char c) {
        return this.inSystem[c] > 0;
    }

    public int status(final char c) {
        final Item item = trie.getDAT()[c];
        return item != null ? item.getStatus() : 0;
    }

    /**
     * 判断一个词语是否在词典中
     */
    public boolean isInSystemDic(final String word) {
        final Item item = trie.getItem(word);
        return item != null && item.getStatus() > 1;
    }

    public AnsjItem getItem(final int index) {
        final AnsjItem item = trie.getItem(index);
        return item != null ? item : AnsjItem.NULL_ITEM;
    }

    public AnsjItem getItem(final String str) {
        final AnsjItem item = trie.getItem(str);
        return item != null ? item : AnsjItem.NULL_ITEM;
    }

    public int getId(final String str) {
        return trie.getId(str);
    }
}
