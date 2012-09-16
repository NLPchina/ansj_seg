package org.ansj.training.name;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 亚洲人名模型训练.训练的时候需要分词.记得屏蔽姓名识别
 * 
 * @author ansj
 * 
 */
public class AsianName {

	private static String base = "data/name";

	public static void main(String[] args) throws IOException, InterruptedException {
		makeFile();
		Thread.sleep(1000L);
		staticFreq();
		initWordFreq();
		System.out.println("训练完毕!序列化到硬盘中了");
	}

	/**
	 * 对人名词表进行分词
	 * 
	 * @throws IOException
	 */
	public static void makeFile() throws IOException {
		String str = null;
		BufferedReader reader = IOUtil.getReader(base + "/asian_name.dic", "UTF-8");
		StringBuilder sb = new StringBuilder();
		while ((str = reader.readLine()) != null) {
			List<Term> terms = ToAnalysis.paser(str);
			if (terms.size() == 1 || terms.size() > 11)
				continue;
			sb.append(terms.toString().replace(", ", " ").replace("[", "").replace("]", ""));
			sb.append("\n");
		}
		reader.close();
		IOUtil.Writer("data/name/asian_name_split.dic", "UTF-8", sb.toString());
	}

	/**
	 * 词频概率表
	 * 
	 * @throws IOException
	 */
	public static void initWordFreq() throws IOException {
		BufferedReader reader = IOUtil.getReader(base + "/asian_name_split.dic", "UTF-8");
		HashMap<String, Entry> hm = new HashMap<String, Entry>();
		String temp = null;
		String[] strs = null;
		Entry entry = null;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(" ");
			for (int i = 0; i < strs.length; i++) {
				entry = hm.get(strs[i]);
				if (entry == null) {
					entry = new Entry();
				}
				entry.add(strs.length, i);
				hm.put(strs[i], entry);
			}
		}

		HashMap<String, int[][]> result = new HashMap<String, int[][]>();

		Set<java.util.Map.Entry<String, Entry>> entrySet = hm.entrySet();
		for (java.util.Map.Entry<String, Entry> entry2 : entrySet) {
			result.put(entry2.getKey(), entry2.getValue().ints);
		}

		IOUtil.WriterObj(base + "/asian_name_freq.data", result);
	}

	/**
	 * 统计词频
	 * 
	 * @throws IOException
	 */
	public static void staticFreq() throws IOException {
		BufferedReader reader = IOUtil.getReader(base + "/asian_name_split.dic", "UTF-8");
		String temp = null;
		int C2 = 0, C3 = 0, C4 = 0;

		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp))
				continue;
			temp = temp.replace(" ", "");
			switch (temp.length()) {
			case 2:
				C2++;
				break;
			case 3:
				C3++;
				break;
			case 4:
				C4++;
				break;
			default:
				System.out.println("error out of freq!");
				break;
			}
		}
		double sum = C2 + C3 + C4;
		System.out.println(C2 / sum);
		System.out.println(C3 / sum);
		System.out.println(C4 / sum);

	}

	static class Entry {
		int[][] ints = new int[3][0];

		public Entry() {
			ints[0] = new int[2];// 2
			ints[1] = new int[3];
			ints[2] = new int[4];
		}

		public void add(int r, int c) {
			ints[r - 2][c]++;
		}
	}
}
