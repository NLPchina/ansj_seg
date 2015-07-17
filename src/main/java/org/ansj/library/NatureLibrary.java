package org.ansj.library;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.ansj.util.MyStaticValue.TAB;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 这里封装了词性和词性之间的关系.以及词性的索引.这是个好东西. 里面数组是从ict里面找来的. 不是很新.没有预料无法训练
 *
 * @author ansj
 */
public class NatureLibrary {

    private static final int ONE = 1;
    private static final int MINUS_ONE = -1;

    /**
     * 词性的字符串对照索引位的hashmap(我发现我又效率狂了.不能这样啊)
     */
    final Map<String, Nature> natureMap;

    /**
     * 词与词之间的关系.对照natureARRAY,natureMap
     */
    final int[][] natureTable;

    // 初始化词性关系和对照表
    public NatureLibrary(final List<String> natureMapLines, final List<String> natureTableLines) {
        final Map<String, Nature> natureMap = new HashMap<>();

        int maxLength = 0;
        for (final String line : natureMapLines) {
            final String[] strs = line.split(TAB);
            if (strs.length == 4) {
                final int p0 = parseInt(strs[0]);
                final int p1 = parseInt(strs[1]);
                final int p2 = parseInt(strs[3]);
                natureMap.put(strs[2], new Nature(strs[2], p0, p1, p2));
                maxLength = Math.max(maxLength, p1);
            }
        }

        final int[][] natureTable = new int[maxLength + 1][maxLength + 1];
        int j = 0;
        for (final String line : natureTableLines) {
            if (isNotBlank(line)) {
                final String[] strs = line.split(TAB);
                for (int i = 0; i < strs.length; i++) {
                    natureTable[j][i] = parseInt(strs[i]);
                }
                j++;
            }
        }

        this.natureMap = natureMap;//ImmutableMap.copyOf(natureMap);
        this.natureTable = natureTable;
    }

    /**
     * 根据字符串得道词性.没有就创建一个
     *
     * @param natureStr natureStr
     * @return nature
     */
    public Nature getNature(final String natureStr) {
        final Nature nature = this.natureMap.get(natureStr);
        if (nature != null) {
            return nature;
        } else {
            final Nature newNature = new Nature(natureStr, MINUS_ONE, MINUS_ONE, ONE);
            this.natureMap.put(natureStr, newNature);
            return newNature;
        }
    }

    /**
     * @param from from
     * @param to   to
     * @return 获得两个词性之间的频率
     */
    public int getNatureFreq(final Nature from, final Nature to) {
        if (from.index < 0 || to.index < 0) {
            return 0;
        }
        return natureTable[from.index][to.index];
    }

    /**
     * @param from from
     * @param to   to
     * @return 获得两个term的nature之间的频率
     */
    public int getNatureFreq(final Term from, final Term to) {
        return getNatureFreq(from.natrue(), to.natrue());
    }
}
