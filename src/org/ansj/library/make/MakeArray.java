package org.ansj.library.make;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import love.cq.util.IOUtil;

import org.ansj.util.MyStaticValue;

public class MakeArray {
	// 终极目标
	public static int base[] = new int[1000000];
	// 验证数组
	public static int check[] = new int[1000000];
	// 词语状态
	public static byte status[] = new byte[1000000];
	// 当前数组的词
	public static String words[] = new String[1000000];
	// natureAndWeight
	public static HashMap<String, Integer> natures[] = new HashMap[1000000];

	private static final HashMap<String, Integer> EN = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> NB = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> PO = new HashMap<String, Integer>();

	static {
		EN.put("en", 1);
		NB.put("nb", 1);
		PO.put("po", 1);
	}

	/**
	 * path 排序好词典的路径 haspath 字符编码字典的路径 arrayPath 生成字典的路径 charEncoding
	 * 关于字典和生成词典的字符编码设置
	 */
	private static String charEncoding = "UTF-8";

	/**
	 * 字典的构建双数组tire树
	 * 
	 * @throws Exception
	 */

	private static int previous;
	private static Map<String, Branch> tempStringMap = new HashMap<String, Branch>();

	public static void makeArray(List<Branch> all) throws Exception {
		// 加载数字词典
		BufferedReader reader = MyStaticValue.getNumberReader();
		makeASCIIArray(reader);
		reader.close();

		// 加载英语词典
		reader = MyStaticValue.getEnglishReader();
		makeASCIIArray(reader);
		reader.close();

		// 加载主词典
		makeBaseArray(all);
		writeLibrary();
		reader.close() ;
	}

	/**
	 * 数组的生成
	 */
	private static void makeBaseArray(List<Branch> all) throws Exception {
		char[] chars = null;
		int length = 0;
		Branch tempValueResult;
		int tempBase = 0;
		String temp = null;
		Branch branch = null;
		for (int i = 0; i < all.size(); i++) {
			branch = all.get(i);
			temp = branch.getValue();
			chars = temp.toCharArray();
			length = chars.length;
			if (length == 1) {
				base[chars[0]] = 65536;
				check[chars[0]] = -1;
				status[chars[0]] = branch.getStatus();
				words[chars[0]] = temp;
				natures[chars[0]] = branch.getNatures();
			} else {
				int previousCheck = getBaseNum(chars);
				if (previous == previousCheck) {
					tempStringMap.put(temp, branch);
					continue;
				}
				if (tempStringMap.size() > 0) {
					setBaseValue(tempStringMap, previous);
					it = tempStringMap.values().iterator();
					while (it.hasNext()) {
						tempValueResult = it.next();
						chars = tempValueResult.getValue().toCharArray();
						tempBase = base[previous] + chars[chars.length - 1];
						base[tempBase] = tempBase;
						check[tempBase] = previous;
						status[tempBase] = tempValueResult.getStatus();
						words[tempBase] = tempValueResult.getValue();
						natures[tempBase] = tempValueResult.getNatures();
					}
				}
				previous = previousCheck;
				tempStringMap = new HashMap<String, Branch>();
				tempStringMap.put(temp, branch);

			}
		}
	}

	public static void makeASCIIArray(BufferedReader reader) throws IOException {
		String temp = null;
		String[] strs = null;
		int baseValue = 0;
		byte sta = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split("\t");
			sta = Byte.parseByte(strs[1]);
			baseValue = strs[0].charAt(0);
			base[baseValue] = baseValue;
			check[baseValue] = -1;
			status[baseValue] = sta;
			words[baseValue] = strs[0];
			switch (sta) {
			case 4:
				natures[baseValue] = EN;
				break;
			case 5:
				natures[baseValue] = NB;
				break;
			case -1:
				natures[baseValue] = PO;
				break;
			}
		}
	}

	/**
	 * 将生成的数组写成词典文件
	 */
	public static void writeLibrary() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < base.length; i++) {
			if (base[i] > 0) {
				sb.append(i + "	" + words[i] + "	" + base[i] + "	" + check[i] + "	" + status[i] + "	" + natures[i]);
				sb.append("\n");
			}
		}
		System.out.println("write ok in ./arrays.dic");
		IOUtil.Writer("./arrays.dic", charEncoding, sb.toString());
	}

	/**
	 * 设置base数组中的父值,使得以父开头的档次都能放到数组中
	 * 
	 * @param tempString
	 *            以父开头单词的全部集合
	 */
	private static Iterator<Branch> it;
	private static char[] chars = null;;

	public static void setBaseValue(Map<String, Branch> tempStringMap, int tempBase) {
		Iterator<String> it = tempStringMap.keySet().iterator();
		while (it.hasNext()) {
			chars = it.next().toCharArray();
			if (!isHave(base[tempBase] + chars[chars.length - 1])) {
				base[tempBase]++;
				it = tempStringMap.keySet().iterator();
			}
		}
	}

	/**
	 * 找到该字符串上一个的位置字符串上一个的位置
	 * 
	 * @param chars
	 *            传入的字符串char数组
	 * @return
	 */
	public static int getBaseNum(char[] chars) {
		int tempBase = 0;
		if (chars.length == 2) {
			return chars[0];
		}
		for (int i = 0; i < chars.length - 1; i++) {
			if (i == 0) {
				tempBase += chars[i];
			} else {
				tempBase = base[tempBase] + chars[i];
			}
		}
		return tempBase;
	}

	/**
	 * 判断在base数组中这个位置是否有这个对象昂
	 * 
	 * @param num
	 *            base数组中的位置
	 * @return
	 */
	public static boolean isHave(int num) {
		if (base[num] > 0) {
			return false;
		} else {
			return true;
		}
	}
}
