package org.ansj.library;

import org.ansj.dic.DicReader;
import org.ansj.domain.AnsjItem;
import org.ansj.domain.NumNatureAttr;
import org.ansj.domain.PersonNatureAttr;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.dat.Item;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DATDictionary {

	private static final Log LOG = LogFactory.getLog(DATDictionary.class);

	/**
	 * 人名补充
	 */
	private static final Map<String, PersonNatureAttr> PERSONMAP = new HashMap<>();

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

			for (char c : NumRecognition.f_NUM) {
				NumNatureAttr numAttr = ((AnsjItem) dat.getDAT()[c]).termNatures.numAttr;
				if (numAttr == null || numAttr == NumNatureAttr.NULL) {
					((AnsjItem) dat.getDAT()[c]).termNatures.numAttr = NumNatureAttr.NUM;
				} else {
					numAttr.setNum(true);
				}
			}

			for (char c : NumRecognition.j_NUM) {
				NumNatureAttr numAttr = ((AnsjItem) dat.getDAT()[c]).termNatures.numAttr;
				if (numAttr == null || numAttr == NumNatureAttr.NULL) {
					((AnsjItem) dat.getDAT()[c]).termNatures.numAttr = NumNatureAttr.NUM;
				} else {
					numAttr.setNum(true);
				}
			}

			// 人名识别必备的
			personNameFull(dat);

			// 记录词典中的词语，并且清除部分数据
			for (Item item : dat.getDAT()) {
				if (item == null || item.getName() == null) {
					continue;
				}
				if (item.getStatus() < 2) {
					item.setName(null);
					continue;
				}
			}
			LOG.info("init core library ok use time : " + (System.currentTimeMillis() - start));
			return dat;
		} catch (InstantiationException e) {
			LOG.warn("无法实例化", e);
		} catch (IllegalAccessException e) {
			LOG.warn("非法访问", e);
		} catch (NumberFormatException e) {
			LOG.warn("数字格式异常", e);
		} catch (IOException e) {
			LOG.warn("IO异常", e);
		}

		return null;
	}

	private static void personNameFull(DoubleArrayTire dat) throws NumberFormatException, IOException {
		BufferedReader reader = null;
		try {
			reader = MyStaticValue.getPersonDicReader();
			AnsjItem item = null;
			String temp = null, word = null;
			float pFreq, freq;

			while ((temp = reader.readLine()) != null) {
				String[] split = temp.split("\t");
				word = split[1];
				pFreq = ObjConver.getFloatValue(split[2]);
				freq = ObjConver.getFloatValue(split[3]);
				item = dat.getItem(word);
				if (item == null || item.getStatus() < 2) {
					PersonNatureAttr pna = PERSONMAP.get(split[1]);
					if (pna == null) {
						pna = new PersonNatureAttr();
					}
					pna.set(temp.charAt(0), (pFreq + 1) / (freq + 1));
					PERSONMAP.put(word, pna);
				} else {
					PersonNatureAttr personAttr = item.termNatures.personAttr;
					if (personAttr == PersonNatureAttr.NULL) {
						personAttr = new PersonNatureAttr();
						item.termNatures.personAttr = personAttr;
					}
					personAttr.set(temp.charAt(0), (pFreq + 1) / (freq + 1));
				}
			}
		} finally {
			reader.close();
		}
	}

	public static int status(char c) {
		Item item = DAT.getDAT()[c];
		if (item == null) {
			return 0;
		}
		return item.getStatus();
	}

	/**
	 * 判断一个词语是否在词典中
	 *
	 * @param word
	 * @return
	 */
	public static boolean isInSystemDic(String word) {
		Item item = DAT.getItem(word);
		return item != null && item.getStatus() > 1;
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
		if (item == null || item.getStatus() < 2) {
			return AnsjItem.NULL;
		}

		return item;
	}

	public static int getId(String str) {
		return DAT.getId(str);
	}

	/**
	 * 取得人名补充
	 *
	 * @param name
	 * @return
	 */
	public static PersonNatureAttr person(String name) {
		return PERSONMAP.get(name);
	}

}
