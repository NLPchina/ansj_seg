package org.ansj.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.library.name.PersonLibrary;
import org.ansj.util.IOUtil;
import org.ansj.util.MyStaticValue;
import org.ansj.util.StringUtil;

public class InitDictionary {
	/**
	 * 所有在词典中出现的词
	 */
	public static final boolean[] IN_SYSTEM = new boolean[65536];
	/**
	 * base: 数组用来存放单词的转换..其实就是一个DFA转换过程
	 */
	public static int[] base = null;
	/**
	 * check: 数组用来验证里面存储的是上一个状态的位置
	 */
	public static int[] check = null;
	/**
	 * status: 用来判断一个单词的状态 1.为不成词.处于过度阶段 2.成次也可能是词语的一部分. 3.词语结束 example: 中 1 中华
	 * 2 中华人 1 中华人民 3
	 */
	public static byte[] status = null;
	/**
	 * words : 数组所在位置的词
	 */
	public static String[] words = null;
	/**
	 * frequency : 词性词典,以及词性的相关权重
	 */
	public static TermNatures[] termNatures = null;


	/**
	 * 两本词典的字符编码
	 */
	private static String charEncoding = "UTF-8";

	/**
	 * 判断词典是否加载过
	 */
	private static boolean isInit = false;

	public static void init() {
		if (!isInit) {
			long start = System.currentTimeMillis();
			try {
				initArrays();
				isInit = true;
				System.out.println("词典加载完成用时:" + (System.currentTimeMillis() - start) + "毫秒");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.print("词典加载出错");
			}
		}
	}

	/**
	 * 对于base,check,natrue,status的加载 0.代表这个字不在词典中 1.继续 2.是个词但是还可以继续 3.停止已经是个词了
	 * 
	 * @throws Exception
	 */
	public static void initArrays() throws Exception {
		BufferedReader reader = MyStaticValue.getArraysReader();
		initArraySize(reader);
		reader.close();
		reader = MyStaticValue.getArraysReader();
		initArrays(reader);
		reader.close();
	}

	private static void initArraySize(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String temp = null;
		String last = null;
		while ((temp = reader.readLine()) != null) {
			last = temp;
		}

		String[] strs = last.split("	");

		int arrayLength = Integer.parseInt(strs[0]) + 1;

		base = new int[arrayLength];

		check = new int[arrayLength];

		status = new byte[arrayLength];

		words = new String[arrayLength];

		// 恶心的java数组泛型
		termNatures = new TermNatures[arrayLength];

	}

	public static void initArrays(BufferedReader reader) throws Exception {
		/**
		 * 人名识别必备的 
		 */
		HashMap<String, PersonNatureAttr> personMap = new PersonLibrary().getPersonMap() ;
		PersonNatureAttr personAttr = null ;
		
		/**
		 * 下面开始加载词典
		 */
		String temp = null;
		String[] strs = null;
		int num = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split("	");
			num = Integer.parseInt(strs[0]);
			base[num] = Integer.parseInt(strs[2]);
			check[num] = Integer.parseInt(strs[3]);
			status[num] = Byte.parseByte(strs[4]);
			if (!"null".equals(strs[5])) {
				words[num] = strs[1];
				if (status[num] < 4) {
					for (int i = 0; i < strs[1].length(); i++) {
						IN_SYSTEM[strs[1].charAt(i)] = true;
					}
				}
				//加载词性
				TermNatures tn = new TermNatures(TermNature.setNatureStrToArray(strs[5]));
				//判断是否是人名属性
				if((personAttr = personMap.get(strs[1]))!=null){
					tn.setPersonNatureAttr(personAttr) ;
				}
				termNatures[num] = tn ;
			}
		}
		reader.close();
	}

	/**
	 * 判断一个词是否在词典中存在
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInSystemDic(String str) {
		init();
		if (StringUtil.isBlank(str)) {
			return true;
		}
		int baseValue = str.charAt(0);
		int checkValue = 0;
		for (int i = 1; i < str.length(); i++) {
			checkValue = baseValue;
			baseValue = base[baseValue] + str.charAt(i);
			if(baseValue>check.length-1) return false ;
			if (check[baseValue] != -1 && check[baseValue] != checkValue) {
				return false;
			}
		}
		return status[baseValue] > 1;
	}
}
