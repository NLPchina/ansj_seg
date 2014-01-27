package org.ansj.library.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.ansj.util.MyStaticValue;

/**
 * 机构名识别词典加载类
 * 
 * @author ansj
 * 
 */
public class CompanyAttrLibrary {
	private static HashMap<String, int[]> cnMap = null;

	private CompanyAttrLibrary() {
	}

	public static HashMap<String, int[]> getCompanyMap() {
		if (cnMap != null) {
			return cnMap;
		}
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			cnMap = new HashMap<String, int[]>();
		}
		return cnMap;
	}

	// company_freq

	private static void init() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		try {
			cnMap = new HashMap<String, int[]>();
			br = MyStaticValue.getCompanReader();
			String temp = null;
			String[] strs = null;
			int[] cna = null;
			while ((temp = br.readLine()) != null) {
				strs = temp.split("\t");
				cna = new int[2];
				cna[0] = Integer.parseInt(strs[1]);
				cna[1] = Integer.parseInt(strs[2]);
				cnMap.put(strs[0], cna);
			}
		} finally {
			if (br != null)
				br.close();
		}
	}

}
