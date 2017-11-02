package org.ansj.train;

import org.ansj.domain.AnsjItem;
import org.nlpcn.commons.lang.dat.DATMaker;
import org.nlpcn.commons.lang.dat.Item;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 构建核心词典
 *
 * @author ansj
 */
public class CoreLibraryMaker {

	private static final Log LOG = LogFactory.getLog();

	public static void main(String[] args) throws Exception {

		makeDic();

		DATMaker datM = new DATMaker();

		datM.maker("train_file/library.txt", AnsjItem.class);

		Item[] dat = datM.getDAT();

		for (int i = '0'; i <= '9'; i++) {
			insertToArray(dat, (char) i, (byte) 5, "{nb=1}");
		}
//		for (int i = '０'; i <= '９'; i++) {
//			insertToArray(dat, (char) i, (byte) 5, "{nb=1}");
//		}


		for (int i = 'a'; i <= 'z'; i++) {
			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
		}

//		for (int i = 'ａ'; i <= 'ｚ'; i++) {
//			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
//		}

//		for (int i = 'Ａ'; i <= 'Ｚ'; i++) {
//			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
//		}
//
//		for (int i = 'A'; i <= 'Z'; i++) {
//			insertToArray(dat, (char) i, (byte) 4, "{en=1}");
//		}

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

		File[] files = new File[]{
				new File("train_file/dic/dic.txt"),
				new File("train_file/dic/two_len.txt"),
				new File("train_file/dic/three_len.txt"),
				new File("train_file/dic/leaders.txt"),
				new File("train_file/dic/dept.txt"),
				new File("train_file/dic/person_newword.txt"),
				new File("train_file/dic/cn_en.txt"),
				new File("train_file/dic/w.txt"),
				new File("train_file/dic/q.txt"),
		};

		Set<String> sets = new HashSet<>();

		TreeMap<String, TreeMap<String, Integer>> dic = new TreeMap<String, TreeMap<String, Integer>>();

		for (File file : files) {

			if(!file.exists()){
				throw  new FileNotFoundException(file.getAbsolutePath()) ;
			}

			BufferedReader br = IOUtil.getReader(file, "utf-8");

			String temp = null;

			int line = 0;

			try {
				while ((temp = br.readLine()) != null) {

					line++;

					if (temp.indexOf('#') > -1) {
						continue;
					}

					temp = temp.replace(String.valueOf(((char) 0)), "");
					String[] split = temp.split("\t");

					String key = split[0] + "\t" + split[1];

					if (sets.contains(key)) {
						LOG.info("skip " + temp);
						continue;
					} else {
						sets.add(key);
					}


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
			} catch (Exception e) {
				LOG.error(file.getName() + "parse line " + line + " error !");
				throw e;
			}
		}

		FileOutputStream fos = new FileOutputStream("train_file/library.txt");

		for (Map.Entry<String, TreeMap<String, Integer>> entry : dic.entrySet()) {
			fos.write((entry.getKey() + "\t" + entry.getValue().toString().replaceAll("\\s+", "")).getBytes("utf-8"));
			fos.write("\n".getBytes());
		}

		fos.flush();
		fos.close();

	}


}
