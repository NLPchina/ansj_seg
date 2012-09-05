package org.ansj.splitWord.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import love.cq.domain.Forest;
import love.cq.domain.WoodInterface;
import love.cq.library.Library;

import org.ansj.domain.Segement;
import org.ansj.util.MyStaticValue;

public class UserDefinedGetWords {
	public static final int MORESTRING = 1;
	public static final int MOREFILE = 0;
	private static Forest FOREST = null;
	private char[] chars;
	private String str;

	/**
	 * 加载用户词典
	 */
	static {
		try {
			FOREST = Library.makeForest(MyStaticValue.rb.getString("userLibrary"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 当前返还单词的偏移量
	 */
	public int offe;
	/**
	 * 记录上一次的偏移量
	 */
	private int tempOffe;

	public UserDefinedGetWords(String content) {
		chars = content.toCharArray();
	}

	public UserDefinedGetWords() {
		chars = new char[0];
	}

	// 当前词语状态
	byte status = 0;
	// 词语分枝
	WoodInterface branch = FOREST;
	// 起始根
	int root = 0;
	int i = root;
	boolean isBack = false;

	public String getStringWords() {
		if (!isBack || i == chars.length - 1) {
			i = root - 1;
		}
		for (i++; i < chars.length; i++) {
			branch = branch.get(chars[i]);
			if (branch == null) {
				root++;
				branch = FOREST;
				i = root - 1;
				isBack = false;
				continue;
			}
			switch (branch.getStatus()) {
			case 2:
				isBack = true;
				offe = tempOffe + root;
				return new String(chars, root, i - root + 1);
			case 3:
				offe = tempOffe + root;
				str = new String(chars, root, i - root + 1);
				branch = FOREST;
				isBack = false;
				root++;
				return str;
			}
		}
		tempOffe += chars.length;
		return null;
	}

	public void setChars(char[] chars) {
		this.chars = chars;
	}

	public void reset() {
		tempOffe = 0;
		branch = FOREST;
	}

	// 是否发现词
	boolean findWord = false;

	public List<Segement> getMaxFrontWordList(String content) {
		if (content == null || content.trim().length() == 0) {
			return Collections.EMPTY_LIST;
		}
		List<Segement> result = new ArrayList<Segement>();
		char[] chars = content.toCharArray();
		// 正文
		StringBuilder sb = new StringBuilder();

		WoodInterface head = FOREST;
		int start = 0;
		int end = 1;
		int index = 0;
		boolean isBack = false;
		int length = chars.length;
		// 此处是正向最大匹配
		for (int i = 0; i < length; i++) {
			index++;
			head = head.get(chars[i]);
			if (head == null) {
				if (isBack) {
					if (sb.length() > 0) {
						result.add(new Segement(sb.toString(), start - sb.length()));
						sb = new StringBuilder();
					}
					result.add(new Segement(new String(chars, start, end), start, "userDefine"));
					start = start + end;
					i = start - 1;
					isBack = false;
				} else {
					sb.append(chars, start, end);
					i = start;
					start++;
				}
				head = FOREST;
				index = 0;
				end = 1;
				continue;
			}
			switch (head.getStatus()) {
			case 1:
				break;
			case 2:
				end = index;
				isBack = true;
				break;
			case 3:
				if (sb.length() > 0) {
					result.add(new Segement(sb.toString(), start - sb.length()));
					sb = new StringBuilder();
				}
				result.add(new Segement(new String(chars, start, index), start, "userDefine"));
				start = start + index;
				index = 0;
				end = 1;
				isBack = false;
				head = FOREST;
				break;
			}
		}

		if (head != FOREST) {
			sb.append(chars, start, index);
			result.add(new Segement(sb.toString(), start));
			sb = new StringBuilder();
		}

		if (sb.length() > 0) {
			result.add(new Segement(sb.toString(), start));
		}

		return result;
	}

	public boolean isE(char c) {
		if ((c >= 'a' && c <= 'z')) {
			return true;
		}
		switch (c) {
		case '.':
			return true;
		case '-':
			return true;
		case '/':
			return true;
		case '#':
			return true;
		case '?':
			return true;
		}
		return false;
	}

	// 此处定义规则
	// status 此字的状态1，继续 2，是个词语但是还可以继续 ,3确定
	// 0继续,1,回退,2是个词但是可以继续,3确定

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();

		UserDefinedGetWords word = new UserDefinedGetWords();
		long start = System.currentTimeMillis();

		System.out.println(word.getMaxFrontWordList("1、不用去搜索了，网上所有的电子版本全部是阉割版和删减版，真正的原文只有这里。包括原著的博客。为什么这里是真全本，不用解释，但请相信绝无第二家。"));

		System.out.println(System.currentTimeMillis() - start);
		System.out.println(sb);
	}
}
