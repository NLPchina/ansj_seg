package org.ansj.library.name;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.ansj.domain.Path;
import org.ansj.domain.PersonNatureAttr;
import org.ansj.util.IOUtil;
import org.ansj.util.MyStaticValue;

/**
 * 人名标注所用的词典就是简单的hashmap简单方便谁用谁知道,只在加载词典的时候用
 * 
 * @author ansj
 * 
 */
public class PersonLibrary {

	private static int[][] PERSONTABLE = null;

	private static final char A = 'A';

	/**
	 * 获得两个词性之间的名称的频率
	 * 
	 * @param ntf
	 *            起始位置的词性
	 * @param ntt
	 *            结束位置的词性
	 * @return 词性的频率
	 */
	public static int getTwoNatureFreq(Path from, Path to) {
		// TODO Auto-generated method stub
		int ntf = from.getTermNature().nature.natureIndex;
		int ntt = to.getTermNature().nature.natureIndex;
		if (ntf < 0 || ntt < 0) {
			return 0;
		}
		return PERSONTABLE[ntf][ntt];
	}

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

	public static void main(String[] args) {
		for (int i = 1; i < 7; i++) {
			System.out.println((char) (44 + 64));
		}
	}
}
