package org.ansj.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 人名标注pojo类
 * 
 * @author ansj
 * 
 */
public class PersonNatureAttr {

	public static final PersonNatureAttr NULL = new PersonNatureAttr();

	public HashMap<Integer, Integer> hm = null;

	// public int B = -1;//1 姓氏
	// public int C = -1;//2 双名的首字
	// public int D = -1;//3 双名的末字
	// public int E = -1;//4 单名
	// public int F = -1;//5 前缀
	// public int G = -1;//6 后缀
	// // *Tag = K( 10), Count = 0, 人名的上文
	// public int L = -1;//11 人名的下文
	// public int M = -1;//12 两个中国人名之间的成分
	// public int N = -1;//13 <无>
	// // *Tag = U( 20), Count = 0, 人名的上文与姓氏成词
	// // *Tag = V( 21), Count = 0, 人名的末字与下文成词
	// public int X = -1;//23 姓与双名首字成词
	// public int Y = -1;//24 姓与单名成词
	// public int Z = -1;//25 双名本身成词
	// public int m = -1;//44 可拆分的姓名
	public boolean flag = false;

	// Tag = *(100), Count = 1, 始##始
	// Tag = *(101), Count = 1, 末##末

	/**
	 * 设置
	 * 
	 * @param index
	 * @param freq
	 */
	public void setFreq(int index, int freq) {
		if (hm == null) {
			hm = new HashMap<Integer, Integer>();
		}
		switch (index) {
		case 1:
			if(freq>10)
			flag = true;
			break;
		case 2:
			break;
		case 4:
			break;
		case 5:
			break;
		case 23:
			break;

		default:
			break;
		}
		hm.put(index, freq);
	}

	public List<Entry<Integer, Integer>> getAttrList() {
		List<Entry<Integer, Integer>> all = new ArrayList<Entry<Integer, Integer>>();
		all.addAll(hm.entrySet());
		return all;
	}

}
