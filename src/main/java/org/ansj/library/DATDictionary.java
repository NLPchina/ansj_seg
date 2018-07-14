package org.ansj.library;

import org.ansj.dic.DicReader;
import org.ansj.domain.AnsjItem;
import org.ansj.domain.NumNatureAttr;
import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.Term;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.dat.Item;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DATDictionary {

	private static final Log LOG = LogFactory.getLog(DATDictionary.class);

	/**
	 * 人名补充
	 */
	private static final Map<String, PersonNatureAttr> PERSONMAP = new HashMap<>();

	/**
	 * 外国人名补充
	 */
	private static final Set<String> FOREIGNSET = new HashSet<>();


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
			float score;

			while ((temp = reader.readLine()) != null) {
				String[] split = temp.split("\t");
				word = split[1];
				score = ObjConver.getFloatValue(split[2]);
				item = dat.getItem(word);
				if (item == null || item.getStatus() < 2) {
					if (word.length() < 2 || word.charAt(0) == ':' || "BEGIN".equals(word) || "END".equals(word)) {
						PersonNatureAttr pna = PERSONMAP.get(split[1]);
						if (pna == null) {
							pna = new PersonNatureAttr();
						}
						pna.set(temp.charAt(0), score);
						PERSONMAP.put(word, pna);
					}
				} else {
					PersonNatureAttr personAttr = item.termNatures.personAttr;
					if (personAttr == PersonNatureAttr.NULL) {
						personAttr = new PersonNatureAttr();
						item.termNatures.personAttr = personAttr;
					}
					personAttr.set(temp.charAt(0), score);
				}
			}
		} finally {
			reader.close();
		}


		try { //将外国人名放入到map中
			reader = MyStaticValue.getForeignDicReader();
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				FOREIGNSET.add(temp);
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

	/**
	 * 取得人名补充
	 *
	 * @param name
	 * @return
	 */
	public static boolean foreign(String name) {
		return FOREIGNSET.contains(name);
	}

	/**
	 * 取得人名补充
	 *
	 * @param term
	 * @return
	 */
	public static boolean foreign(Term term) {
		String name = term.getName();

		boolean contains = FOREIGNSET.contains(name);
		if (contains) {
			return contains;
		}

		if (!term.getNatureStr().startsWith("nr")) {
			return false;
		}

		for (int i = 0; i < name.length(); i++) {
			if (!FOREIGNSET.contains(String.valueOf(name.charAt(i)))) {
				return false;
			}
		}
		return true ;
	}


	public static void write2File(String path) throws IOException {
		ObjectOutput oop = new ObjectOutputStream(new FileOutputStream(new File(path)));

		oop.writeObject(DAT.getDAT());

		oop.writeObject(PERSONMAP);

		oop.writeObject(FOREIGNSET);

		oop.flush();

		oop.close();
	}


	public static DoubleArrayTire loadFromFile(String path) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));

		Item[] items = (Item[]) ois.readObject();

		DoubleArrayTire dat = new DoubleArrayTire(items);


		PERSONMAP.putAll(((Map<String, PersonNatureAttr>) ois.readObject()));

		FOREIGNSET.addAll(((Set<String>) ois.readObject()));

		return dat;
	}


}
