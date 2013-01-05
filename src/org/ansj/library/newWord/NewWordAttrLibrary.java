package org.ansj.library.newWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.ansj.domain.NewWordNatureAttr;
import org.ansj.util.MyStaticValue;

/**
 * 新词发现的词典加载
 * 
 * @author ansj
 * 
 */
public class NewWordAttrLibrary {
	private HashMap<String, NewWordNatureAttr> nwMap = null;

	public NewWordAttrLibrary() {
	}

	public HashMap<String, NewWordNatureAttr> getNewWordMap() throws NumberFormatException, IOException {
		if (nwMap != null) {
			return nwMap;
		}
		init();
		return nwMap;
	}

	// company_freq

	private void init() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		try {
			nwMap = new HashMap<String, NewWordNatureAttr>();
			br = MyStaticValue.getNewWordReader();
			String temp = null;
			String[] strs = null;
			NewWordNatureAttr nna = null;

			int b = 0;
			int m = 0;
			int e = 0;

			while ((temp = br.readLine()) != null) {
				strs = temp.split("\t");
				b = Integer.parseInt(strs[1]);
				m = Integer.parseInt(strs[2]);
				e = Integer.parseInt(strs[3]);
				nna = new NewWordNatureAttr(b, m, e);
				nwMap.put(strs[0], nna);
			}
		} finally {
			if (br != null)
				br.close();
		}
	}
}
