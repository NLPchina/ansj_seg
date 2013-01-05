package org.ansj.library.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import love.cq.util.StringUtil;

import org.ansj.dic.DicReader;
import org.ansj.domain.CompanyNatureAttr;
import org.ansj.util.MyStaticValue;

/**
 * 机构名识别词典加载类
 * 
 * @author ansj
 * 
 */
public class CompanyAttrLibrary {
	private HashMap<String, CompanyNatureAttr> cnMap = null;

	public CompanyAttrLibrary() {
	}

	public HashMap<String, CompanyNatureAttr> getCompanyMap() throws NumberFormatException, IOException {
		if (cnMap != null) {
			return cnMap;
		}
		init();
		return cnMap;
	}

	// company_freq

	private void init() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		try {
			cnMap = new HashMap<String, CompanyNatureAttr>();
			br = MyStaticValue.getCompanReader();
			String temp = null;
			String[] strs = null;
			CompanyNatureAttr cna = null;

			int p = 0;
			int b = 0;
			int m = 0;
			int e = 0;
			int s = 0;
			int allFreq = 0 ;

			while ((temp = br.readLine()) != null) {
				strs = temp.split("\t");
				p = Integer.parseInt(strs[1]);
				b = Integer.parseInt(strs[2]);
				m = Integer.parseInt(strs[3]);
				e = Integer.parseInt(strs[4]);
				s = Integer.parseInt(strs[5]);
				allFreq = Integer.parseInt(strs[6]);
				cna = new CompanyNatureAttr(p, b, m, e, s,allFreq);
				cnMap.put(strs[0], cna);
			}
		} finally {
			if (br != null)
				br.close();
		}
	}

	public static double[] loadFactory() {
		// TODO Auto-generated method stub
		BufferedReader reader = DicReader.getReader("company/company.map");
		String temp = null;
		double[] factory = new double[51];
		String[] strs = null;
		int index = 0;
		float fac = 0;
		try {
			while ((temp = reader.readLine()) != null) {
				if (StringUtil.isBlank(temp = temp.trim())) {
					continue;
				}
				strs = temp.split("\t");
				index = Integer.parseInt(strs[0]);
				fac = Float.parseFloat(strs[2]);
				if (index > 50) {
					index = 50;
				}
				factory[index] += fac;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("公司频率词典加载失败!");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return factory;
	}
}
