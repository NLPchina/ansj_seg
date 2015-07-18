package org.ansj.library.name;

import lombok.SneakyThrows;
import org.ansj.domain.PersonNatureAttr;
import org.ansj.util.MyStaticValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map.Entry;

import static java.lang.Integer.parseInt;
import static org.ansj.domain.PersonNatureAttr.NULL_PERSON_NATURE_ATTR;

/**
 * 人名标注所用的词典就是简单的hashmap简单方便谁用谁知道,只在加载词典的时候用
 *
 * @author ansj
 */

public class PersonAttrLibrary {

    private HashMap<String, PersonNatureAttr> pnMap;

    public HashMap<String, PersonNatureAttr> getPersonMap(final BufferedReader personReader) {
        if (this.pnMap == null) {
            this.pnMap = initPnMap(personReader);
        }
        return this.pnMap;
    }

    @SneakyThrows
    private static HashMap<String, PersonNatureAttr> initPnMap(final BufferedReader personReader) {
        // person.dic
        final HashMap<String, PersonNatureAttr> pnMap = new HashMap<>();
        try {
            String temp;
            while ((temp = personReader.readLine()) != null) {
                final String[] strs = temp.split("\t");
                final PersonNatureAttr pna = pnMap.get(strs[0]);
                pnMap.put(
                        strs[0],
                        pna != null ?
                                pna.addFreq(parseInt(strs[1]), parseInt(strs[2])) :
                                NULL_PERSON_NATURE_ATTR.addFreq(parseInt(strs[1]), parseInt(strs[2]))
                );
            }
        } finally {
            personReader.close();
        }

        // name_freq
        for (final Entry<String, int[][]> entry : MyStaticValue.getPersonFreqMap().entrySet()) {
            final PersonNatureAttr pna = pnMap.get(entry.getKey());
            pnMap.put(
                    entry.getKey(),
                    pna != null ?
                            pna.withNewLocFreq(entry.getValue()) :
                            NULL_PERSON_NATURE_ATTR.withNewLocFreq(entry.getValue())
            );
        }
        return pnMap;
    }
}
