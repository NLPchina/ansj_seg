package org.ansj.library;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.ansj.util.AnsjContext.TAB;

/**
 * 机构名识别词典加载类
 *
 * @author ansj
 */
public class CompanyAttrLibrary {

    private final Map<String, int[]> map;

    public CompanyAttrLibrary(final List<String> lines) {
        final HashMap<String, int[]> cnMap = new LinkedHashMap<>();
        for (final String line : lines) {
            final String[] strs = line.split(TAB);
            final int[] cna = new int[]{
                    parseInt(strs[1]),
                    parseInt(strs[2])
            };
            cnMap.put(strs[0], cna);
        }
        this.map = ImmutableMap.copyOf(cnMap);
    }

    public Map<String, int[]> getCompanyMap() {
        return this.map;
    }
}
