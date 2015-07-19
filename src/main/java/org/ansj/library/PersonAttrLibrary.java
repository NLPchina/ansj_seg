package org.ansj.library;

import com.google.common.collect.ImmutableMap;
import org.ansj.domain.PersonNatureAttr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.Integer.parseInt;
import static org.ansj.domain.PersonNatureAttr.NULL_PERSON_NATURE_ATTR;
import static org.ansj.util.AnsjContext.TAB;

/**
 * 人名标注所用的词典就是简单的hashmap简单方便谁用谁知道,只在加载词典的时候用
 *
 * @author ansj
 */

public class PersonAttrLibrary {

    private final Map<String, PersonNatureAttr> map;

    public PersonAttrLibrary(final List<String> personDicLines, final Map<String, int[][]> personFreqMap) {
        // person.dic
        final HashMap<String, PersonNatureAttr> pnMap = new HashMap<>();
        for (final String line : personDicLines) {
            final String[] strs = line.split(TAB);
            final PersonNatureAttr pna = pnMap.get(strs[0]);
            pnMap.put(
                    strs[0],
                    pna != null ?
                            pna.addFreq(parseInt(strs[1]), parseInt(strs[2])) :
                            NULL_PERSON_NATURE_ATTR.addFreq(parseInt(strs[1]), parseInt(strs[2]))
            );
        }
        // name_freq
        for (final Entry<String, int[][]> entry : personFreqMap.entrySet()) {
            final PersonNatureAttr pna = pnMap.get(entry.getKey());
            pnMap.put(
                    entry.getKey(),
                    pna != null ?
                            pna.withNewLocFreq(entry.getValue()) :
                            NULL_PERSON_NATURE_ATTR.withNewLocFreq(entry.getValue())
            );
        }
        this.map = ImmutableMap.copyOf(pnMap);
    }

    public Map<String, PersonNatureAttr> getPersonMap() {
        return this.map;
    }
}
