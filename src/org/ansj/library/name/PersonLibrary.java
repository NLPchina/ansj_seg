package org.ansj.library.name;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.ansj.domain.PersonNatureAttr;
import org.ansj.util.MyStaticValue;

/**
 * 人名标注所用的词典就是简单的hashmap简单方便谁用谁知道,只在加载词典的时候用
 * 
 * @author ansj
 * 
 */
public class PersonLibrary {


	private HashMap<String, PersonNatureAttr> pnMap = null;

	public PersonLibrary() {
	}

	public HashMap<String, PersonNatureAttr> getPersonMap() throws NumberFormatException, IOException {
		BufferedReader br = null;
		try {
			if (pnMap != null) {
				return pnMap;
			}
			pnMap = new HashMap<String, PersonNatureAttr>();
			br = MyStaticValue.getPersonReader();
			String temp = null;
			String[] strs = null;
			PersonNatureAttr pna = null;
			while ((temp = br.readLine()) != null) {
				pna = new PersonNatureAttr();
				strs = temp.split("\t");
				pna = pnMap.get(strs[0]);
				if (pna == null) {
					pna = new PersonNatureAttr();
				}
				pna.setFreq(Integer.parseInt(strs[1]), Integer.parseInt(strs[2]));
				pnMap.put(strs[0], pna) ;
			}

			return pnMap;
		} finally {
			if (br != null)
				br.close();
		}
	}
}
