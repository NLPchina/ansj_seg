package org.ansj.app.crf.pojo;

import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析crf++模板
 *
 * @author ansj
 */
public class Template implements Serializable {

    private static final long serialVersionUID = 8265350854930361325L;

    public final int[][] ft;

    public final int left;

    public final int right;

    public Template() {
        this(
                new int[][]{{-2}, {-1}, {0}, {1}, {2}, {-2, -1}, {-1, 0}, {0, 1}, {1, 2}, {-1, 1}},
                2,
                2,
                0,
                new HashMap<>()
        );
    }

    public Template(final int[][] ft, final int left, final int right) {
        this(
                ft,
                left,
                right,
                0,
                new HashMap<>()
        );
    }

    private Template(
            final int[][] ft,
            final int left,
            final int right,
            final int tagNum,
            final Map<String, Integer> statusMap
    ) {
        this.ft = ft;
        this.left = left;
        this.right = right;
        this.tagNum = tagNum;
        this.statusMap = statusMap != null ? ImmutableMap.copyOf(statusMap) : null;
    }

    public final int tagNum;

    public final Map<String, Integer> statusMap;

    public Template withTagNum(final int tagNum) {
        return new Template(
                this.ft,
                this.left,
                this.right,
                tagNum,
                this.statusMap
        );
    }

    public Template withStatusMap(final Map<String, Integer> statusMap) {
        return new Template(
                this.ft,
                this.left,
                this.right,
                this.tagNum,
                statusMap
        );
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder()
                .append("left:").append(this.left).append("\t")
                .append("rightr:").append(this.right).append("\n");
        for (final int[] ints : this.ft) {
            sb.append(Arrays.toString(ints)).append("\n");
        }
        return sb.toString();
    }
}
