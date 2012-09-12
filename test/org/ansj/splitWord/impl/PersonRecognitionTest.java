package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import love.cq.util.IOUtil;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.StringUtil;
import org.ansj.util.recognition.PersonRecognition;

public class PersonRecognitionTest {
	public static void main(String[] args) throws Exception {
		List<String> list = new ArrayList<String>();
		String str = null;
		list.add("李宇春《再不疯狂我们就老了》MV首播】李宇春新专辑同名第二主打《再不疯狂我们就老了》MV今日正式发布。这首歌与《似火年华》，以“疯狂”为概念的对话曲目，采用一曲双词的方式。李宇春与韩寒，同时在一首歌里，讲述了两种截然相反，却本质同归的态度");
		list.add("上个月在天津术语学会上见到冯老，言谈中感觉到冯老对机器翻译的深厚感情和殷切希望。是啊，机器翻译事业还年轻，我辈细流，心驰沧海，愿倾尽绵薄之力，浇灌此常青之树。");
		list.add("发表了博文 《多语言信息网络时代的语言学家：冯志伟》 - 冯志伟与老伴郑初阳 多语言信息网络时代的语言学家：冯志伟 桂清扬 冯志伟，教育部语言文字应用研究所研究员，博士生导师，所学术委员会");
		list.add("Facebook CEO 马克·扎克伯格亮相了周二 TechCrunch Disrupt 大会，并针对公司不断下挫的股价、移动战略、广告业务等方面发表了讲话。自 5 月公司 IPO 后，扎克伯格极少公开露面，这也是他首次在重要场合公开接受采访");
		list.add("@新华社中国网事：#聚焦钓鱼岛#外交部长杨洁篪10日在外交部紧急召见日本驻华大使丹羽宇一郎，就日本政府非法“购买”钓鱼岛提出严正交涉和强烈抗议。当日，中国驻日本大使程永华也向日本外务省负责人提出严正交涉并递交了抗议照会。");
		list.add("阿米尔汗，8岁时出演一部轰动印度的电影，是公认的童星，长大后却一心打网球并获得过网球冠军。21岁爱上邻居家女孩，由于宗教原因两人决定私奔，现在过着幸福美满的生活。81届奥斯卡最佳影片《贫民窟的百万富翁》，他担任制片。2009年一部《三个白痴》震惊全球，他47岁");
		list.add("老郭动粗 师徒揭相声虚假繁荣");
		for (String string : list) {
			List<Term> paser = ToAnalysis.paser(string);
			System.out.println(paser);
		}
//		initWordFreq() ;
	}

	public static void makeFile() throws IOException {
		String str = null;
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/library/name.dic", "UTF-8");
		HashMap<String, Integer> hm = new HashMap();
		StringBuilder sb = new StringBuilder();
		while ((str = reader.readLine()) != null) {
			List<Term> terms = ToAnalysis.paser(str);
			if (terms.size() == 1 || terms.size() > 11)
				continue;
			sb.append(terms);
			sb.append("\n");
		}
		reader.close();
		IOUtil.Writer("/Users/ansj/Documents/快盘/分词/library/name_split.dic", "UTF-8", sb.toString());
	}

	public static void staticesFile() throws Exception {
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/library/name_split.dic", "UTF-8");
		HashMap<String, int[]> hm = new HashMap<String, int[]>();
		String temp = null;
		String[] strs = null;
		int[] ints = null;
		int min = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(" ");
			for (int i = 0; i < strs.length; i++) {
				min = Math.min(i, 4);
				if ((ints = hm.get(strs[i])) != null) {
					ints[min]++;
				} else {
					ints = new int[5];
					ints[min]++;
					hm.put(strs[min], ints);
				}
			}
		}

		reader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/library/name_temp.dic", "UTF-8");
		while ((temp = reader.readLine()) != null) {
			strs = temp.split("\t");
			if (strs.length < 3)
				continue;
			if (Integer.parseInt(strs[1]) == 23) {
				min = 1;
			} else {
				min = 2;
			}
			int freq = Integer.parseInt(strs[2]);
			if ((ints = hm.get(strs[0])) != null) {
				ints[min] += freq;
			} else {
				ints = new int[5];
				ints[min] += freq;
				hm.put(strs[min], ints);
			}
		}

		StringBuilder sb = new StringBuilder();

//		Set<Entry<String, int[]>> entrySet = hm.entrySet();
//		for (Entry<String, int[]> entry : entrySet) {
//			ints = entry.getValue();
//			sb.append(entry.getKey());
//			sb.append("\t");
//			sb.append(ints[0]);
//			sb.append("\t");
//			sb.append(ints[1]);
//			sb.append("\t");
//			sb.append(ints[2]);
//			sb.append("\t");
//			sb.append(ints[3]);
//			sb.append("\t");
//			sb.append(ints[4]);
//			sb.append("\n");
//		}

		IOUtil.Writer("/Users/ansj/Documents/快盘/分词/library/name_result.dic", "UTF-8", sb.toString());
	}

	public static void staticFreq() throws IOException {
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/library/name.dic", "UTF-8");
		HashMap<String, int[]> hm = new HashMap<String, int[]>();
		String temp = null;
		String[] strs = null;
		int[] ints = null;
		int min = 0;
		int C2 = 0, C3 = 0, C4 = 0, CN = 0;

		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp))
				continue;
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
				CN++;
				break;
			}
		}
		double sum = C2 + C3 + C4 + CN;
		System.out.println(C2 / sum);
		System.out.println(C3 / sum);
		System.out.println(C4 / sum);
		System.out.println(CN / sum);

	}

	public static void initWordFreq() throws IOException {
		BufferedReader reader = IOUtil.getReader("/Users/ansj/Documents/快盘/分词/library/name_split.dic", "UTF-8");
		HashMap<String, Entry> hm = new HashMap<String, Entry>();
		String temp = null;
		String[] strs = null;
		int min = 0;
		Entry entry = null;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(" ");
			for (int i = 0; i < strs.length; i++) {
				if(strs.length<=1) continue ;
				min = Math.min(i, 4);
				entry = hm.get(strs[i]);
				if (entry == null) {
					entry = new Entry();
				}
				entry.add(strs.length, min);
				hm.put(strs[i], entry);
			}
		}
		
		entry = new Entry() ;
		hm.put("·", entry) ;
		
		for (int i = 0; i < 10; i++) {
			entry.add(4, 1) ;
			entry.add(4, 2) ;
			entry.add(5, 1) ;
			entry.add(5, 2) ;
			entry.add(5, 3) ;
			entry.add(5, 4) ;
		}
		
		
		HashMap<String,int[][]> result = new HashMap<String,int[][]>() ;
		
		Set<java.util.Map.Entry<String, Entry>> entrySet = hm.entrySet() ;
		for (java.util.Map.Entry<String, Entry> entry2 : entrySet) {
			result.put(entry2.getKey(), entry2.getValue().ints) ;
		}
		
		IOUtil.WriterObj("name_freq.data", result) ;
		System.out.println(result.get("和"));
		System.out.println(result.get("的"));
		
	}

	static class Entry implements Serializable {
		int[][] ints = new int[4][0];

		public Entry() {
			ints[0] = new int[2];// 2
			ints[1] = new int[3];
			ints[2] = new int[4];
			ints[3] = new int[5];
		}

		public void add(int r, int c) {
			if(r>3) r =5 ;
			ints[r - 2][c]++;
		}
	}

}