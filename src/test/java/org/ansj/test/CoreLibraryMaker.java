package org.ansj.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.TreeMap;

import org.ansj.domain.AnsjItem;
import org.nlpcn.commons.lang.dat.DATMaker;
import org.nlpcn.commons.lang.dat.Item;
import org.nlpcn.commons.lang.util.IOUtil;

/**
 * 构建核心词典
 * 
 * @author ansj
 * 
 */
public class CoreLibraryMaker {
	public static void main(String[] args) throws Exception {
		makeDic();
		DATMaker datM = new DATMaker();

		datM.maker("train_file/library.txt", AnsjItem.class);

		Item[] dat = datM.getDAT();

		insertToArray(dat, '%', (byte) 5, "{nb=1}");
		insertToArray(dat, '.', (byte) 5, "{nb=1}");
		for (int i = '0'; i <= '9'; i++) {
			insertToArray(dat, (char) i, (byte) 5, "{nb=1}");
		}
		for (int i = '０'; i <= '９'; i++) {
			insertToArray(dat, (char) i, (byte) 5, "{nb=1}");
		}

		insertToArray(dat, (char) '\'', (byte) 4, "{en=1}");

		for (int i = 'a'; i <= 'z'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}

		for (int i = 'ａ'; i <= 'ｚ'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}

		for (int i = 'Ａ'; i <= 'Ｚ'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}

		for (int i = 'A'; i <= 'Z'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}

		datM.saveText("src/main/resources/core.dic");

	}

	private static void insertToArray(Item[] dat, char c, byte status, String param) {
		AnsjItem ansjItem1 = new AnsjItem();
		ansjItem1.setName(String.valueOf(c));
		ansjItem1.setIndex(c);
		ansjItem1.setCheck(-1);
		ansjItem1.setStatus(status);
		ansjItem1.param = param;
		dat[c] = ansjItem1;
	}

	public static void makeDic() throws NumberFormatException, IOException {
		BufferedReader br = IOUtil.getReader("train_file/dic.txt", "utf-8");

		String temp = null;

		TreeMap<String, TreeMap<String, Integer>> dic = new TreeMap<String, TreeMap<String, Integer>>();

		while ((temp = br.readLine()) != null) {

			if (temp.indexOf('#') > -1) {
				continue;
			}

			temp = temp.replace(String.valueOf(((char) 0)), "");
			String[] split = temp.split("\t");
			if (dic.containsKey(split[1])) {
				if (dic.get(split[1]).containsKey(split[0])) {
					System.out.println("err");
				}
				dic.get(split[1]).put(split[0], Integer.parseInt(split[2]));
			} else {
				TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
				tm.put(split[0], Integer.parseInt(split[2]));
				dic.put(split[1], tm);
			}
		}

		IOUtil.writeMap(dic, "train_file/library.txt", IOUtil.UTF8);
	}
}
