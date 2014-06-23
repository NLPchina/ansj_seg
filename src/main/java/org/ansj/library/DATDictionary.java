package org.ansj.library;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.ansj.dic.DicReader;
import org.ansj.domain.AnsjItem;
import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.library.name.PersonAttrLibrary;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.dat.Item;

public class DATDictionary {

	/**
	 * 所有在词典中出现的词,并且承担简繁体转换的任务.
	 */
	public static final char[] IN_SYSTEM = new char[65536];

	/**
	 * 核心词典
	 */
	private static final DoubleArrayTire DAT = loadDAT();

	/**
	 * 数组长度
	 */
	public static int arrayLength = DAT.arrayLength;

	/**
	 * 加载词典
	 * 
	 * @return
	 */
	private static DoubleArrayTire loadDAT() {

		long start = System.currentTimeMillis();

		try {

			DoubleArrayTire dat = DoubleArrayTire.loadText(DicReader.getInputStream("core.dic"), AnsjItem.class);

			/**
			 * 人名识别必备的
			 */
			personNameFull(dat);

			/**
			 * 记录词典中的词语，并且清除部分数据
			 */

			for (Item item : dat.getDAT()) {
				if (item == null || item.name == null) {
					continue;
				}

				if (item.status < 4) {
					for (int i = 0; i < item.name.length(); i++) {
						IN_SYSTEM[item.name.charAt(i)] = item.name.charAt(i);
					}
				}
				if (item.status < 2) {
					item.name = null;
					continue;
				}
			}
			// 特殊字符标准化
			IN_SYSTEM['％'] = '%';

			MyStaticValue.LIBRARYLOG.info("init core library ok use time :" + (System.currentTimeMillis() - start));

			return dat;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static void personNameFull(DoubleArrayTire dat) throws NumberFormatException, IOException {
		HashMap<String, PersonNatureAttr> personMap = new PersonAttrLibrary().getPersonMap();

		AnsjItem ansjItem = null;
		// 人名词性补录
		Set<Entry<String, PersonNatureAttr>> entrySet = personMap.entrySet();
		char c = 0;
		String temp = null;
		for (Entry<String, PersonNatureAttr> entry : entrySet) {
			temp = entry.getKey();

			if (temp.length() == 1 && (ansjItem = (AnsjItem) dat.getDAT()[temp.charAt(0)]) == null) {
				ansjItem = new AnsjItem();
				ansjItem.base = c;
				ansjItem.check = -1;
				ansjItem.status = 3;
				ansjItem.name = temp;
				dat.getDAT()[temp.charAt(0)] = ansjItem;
			} else {
				ansjItem = dat.getItem(temp);
			}

			if (ansjItem == null) {
				continue;
			}

			if ((ansjItem.termNatures) == null) {
				ansjItem.termNatures = new TermNatures(TermNature.NR);
			}
			ansjItem.termNatures.setPersonNatureAttr(entry.getValue());
		}
	}

	public static int status(char c) {
		Item item = (AnsjItem) DAT.getDAT()[c];
		if (item == null) {
			return 0;
		}
		return item.status;
	}

	/**
	 * 判断一个词语是否在词典中
	 * 
	 * @param word
	 * @return
	 */
	public static boolean isInSystemDic(String word) {
		Item item = DAT.getItem(word);
		return item != null && item.status > 1;
	}

	public static AnsjItem getItem(int index) {
		AnsjItem item = DAT.getItem(index);
		if (item == null) {
			return AnsjItem.NULL;
		}

		return item;
	}

	public static AnsjItem getItem(String str) {
		AnsjItem item = DAT.getItem(str);
		if (item == null) {
			return AnsjItem.NULL;
		}

		return item;
	}

	public static int getId(String str) {
		return DAT.getId(str);
	}

}
