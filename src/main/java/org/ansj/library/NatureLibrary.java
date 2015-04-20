package org.ansj.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 这里封装了词性和词性之间的关系.以及词性的索引.这是个好东西. 里面数组是从ict里面找来的. 不是很新.没有预料无法训练
 * 
 * @author ansj
 * 
 */
public class NatureLibrary {

	private static final int YI = 1;
	private static final int FYI = -1;
	/**
	 * 词性的字符串对照索引位的hashmap(我发现我又效率狂了.不能这样啊)
	 */
	private static final HashMap<String, Nature> NATUREMAP = new HashMap<String, Nature>();

	/**
	 * 词与词之间的关系.对照natureARRAY,natureMap
	 */
	private static int[][] NATURETABLE = null;

	/**
	 * 初始化对照表
	 */
	static {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("词性列表加载失败!");
		}
	}

	private static void init() throws IOException {
		String split = "\t";
		// 加载词对照性表
		BufferedReader reader = MyStaticValue.getNatureMapReader();
		String temp = null;
		String[] strs = null;
		int maxLength = 0;
		int p0 = 0;
		int p1 = 0;
		int p2 = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(split);
			if (strs.length != 4)
				continue;

			p0 = Integer.parseInt(strs[0]);
			p1 = Integer.parseInt(strs[1]);
			p2 = Integer.parseInt(strs[3]);
			NATUREMAP.put(strs[2], new Nature(strs[2], p0, p1, p2));
			maxLength = Math.max(maxLength, p1);
		}
		reader.close();

		// 加载词性关系
		NATURETABLE = new int[maxLength + 1][maxLength + 1];
		reader = MyStaticValue.getNatureTableReader();
		int j = 0;
		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp))
				continue;
			strs = temp.split(split);
			for (int i = 0; i < strs.length; i++) {
				NATURETABLE[j][i] = Integer.parseInt(strs[i]);
			}
			j++;
		}
		reader.close();
	}

	/**
	 * 获得两个词性之间的频率
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getTwoNatureFreq(Nature from, Nature to) {
		if (from.index < 0 || to.index < 0) {
			return 0;
		}
		return NATURETABLE[from.index][to.index];
	}

	/**
	 * 获得两个term之间的频率
	 * 
	 * @param fromTerm
	 * @param toTerm
	 * @return
	 */
	public static int getTwoTermFreq(Term fromTerm, Term toTerm) {
		Nature from = fromTerm.natrue();
		Nature to = toTerm.natrue();
		if (from.index < 0 || to.index < 0) {
			return 0;
		}
		return NATURETABLE[from.index][to.index];
	}

	/**
	 * 根据字符串得道词性.没有就创建一个
	 * 
	 * @param natureStr
	 * @return
	 */
	public static Nature getNature(String natureStr) {
		Nature nature = NATUREMAP.get(natureStr);
		if (nature == null) {
			nature = new Nature(natureStr, FYI, FYI, YI);
			NATUREMAP.put(natureStr, nature);
			return nature;
		}
		return nature;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(NATURETABLE[NATUREMAP.get("nr").natureIndex][NATUREMAP.get("mq").natureIndex]);

	}

}
