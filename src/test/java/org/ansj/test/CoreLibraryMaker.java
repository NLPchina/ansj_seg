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
		makeDic() ;
		DATMaker datM = new DATMaker();

		datM.maker("train_file/library.txt", AnsjItem.class);

		Item[] dat = datM.getDAT();

		AnsjItem ansjItem1 = new AnsjItem();
		ansjItem1.name = String.valueOf((char) '%');
		ansjItem1.index = '%';
		ansjItem1.check = -1;
		ansjItem1.status = 5;
		ansjItem1.param = "{nb=1}";
		dat['%'] = ansjItem1;
		for (int i = '0'; i <= '9'; i++) {
			AnsjItem ansjItem = new AnsjItem();
			ansjItem.name = String.valueOf((char) i);
			ansjItem.index = i;
			ansjItem.check = -1;
			ansjItem.status = 5;
			ansjItem.param = "{nb=1}";
			dat[i] = ansjItem;
		}

		AnsjItem ansjItem2 = new AnsjItem();
		ansjItem2.name = String.valueOf((char) '\'');
		ansjItem2.check = -1;
		ansjItem2.status = 4;
		ansjItem2.index = '\'';
		ansjItem2.param = "{en=1}";
		dat['\''] = ansjItem2;
		for (int i = 'a'; i <= 'z'; i++) {
			AnsjItem ansjItem = new AnsjItem();
			ansjItem.name = String.valueOf((char) i);
			ansjItem.index = i;
			ansjItem.check = -1;
			ansjItem.status = 4;
			ansjItem.param = "{en=1}";
			dat[i] = ansjItem;
		}

		datM.saveText("src/main/resources/core.dic");

	}

	public static void makeDic() throws NumberFormatException, IOException {
		BufferedReader br = IOUtil.getReader("train_file/dic.txt", "utf-8");

		String temp = null;

		TreeMap<String, TreeMap<String, Integer>> dic = new TreeMap<String, TreeMap<String, Integer>>();

		while ((temp = br.readLine()) != null) {
			
			if(temp.indexOf('#')>-1){
				continue ;
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
