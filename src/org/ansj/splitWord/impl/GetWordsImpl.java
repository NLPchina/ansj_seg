package org.ansj.splitWord.impl;

import static org.ansj.library.InitDictionary.base;
import static org.ansj.library.InitDictionary.check;
import static org.ansj.library.InitDictionary.conversion;
import static org.ansj.library.InitDictionary.status;
import static org.ansj.library.InitDictionary.termNatures;
import static org.ansj.library.InitDictionary.words;

import org.ansj.domain.TermNatures;
import org.ansj.library.InitDictionary;
import org.ansj.splitWord.GetWords;

public class GetWordsImpl implements GetWords {

	/**
	 * offe : 当前词的偏移量
	 */
	public int offe;

	/**
	 * 构造方法，同时加载词典,传入词语相当于同时调用了setStr() ;
	 */
	public GetWordsImpl(String str) {
		setStr(str);
	}

	/**
	 * 构造方法，同时加载词典
	 */
	public GetWordsImpl() {
	}

	int charsLength = 0;

	public void setStr(String chars) {
		this.chars = chars;
		charsLength = chars.length();
		checkValue = 0;
	}

	public String chars;
	private int charHashCode;
	private int start = 0;
	public int end = 0;
	private int baseValue = 0;
	private int checkValue = 0;
	private int tempBaseValue = 0;
	public int i = 0;
	private String str = null;

	public String allWords() {
		for (; i < charsLength; i++) {
			charHashCode = conversion(chars.charAt(i));
			end++;
			switch (getStatement()) {
			case 0:
				if (baseValue == chars.charAt(i)) {
					str = "" + chars.charAt(i);
					offe = i;
					start = ++i;
					end = 0;
					baseValue = 0;
					tempBaseValue = baseValue;
					return str;
				} else {
					i = start;
					start++;
					end = 0;
					baseValue = 0;
					break;
				}
			case 2:
				i++;
				offe = start;
				tempBaseValue = baseValue;
				return words[tempBaseValue];
			case 3:
				offe = start;
				start++;
				i = start;
				end = 0;
				tempBaseValue = baseValue;
				baseValue = 0;
				return words[tempBaseValue];
			}

		}
		if (start++ != i) {
			i = start;
			baseValue = 0 ;
			return allWords();
		}
		start = 0;
		end = 0;
		baseValue = 0;
		i = 0;
		return null;
	}

	/**
	 * 根据用户传入的c得到单词的状态. 0.代表这个字不在词典中 1.继续 2.是个词但是还可以继续 3.停止已经是个词了
	 * 
	 * @param c
	 * @return
	 */
	private int getStatement() {
		checkValue = baseValue;
		baseValue = base[checkValue] + charHashCode;
		if (check[baseValue] == checkValue || check[baseValue] == -1) {
			return status[baseValue];
		}
		return 0;
	}

	public byte getStatus() {
		// TODO Auto-generated method stub
		return status[tempBaseValue];
	}

	/**
	 * 获得当前词的词性
	 * 
	 * @return
	 */
	public TermNatures getTermNatures() {
		// TODO Auto-generated method stub
		TermNatures tns = termNatures[tempBaseValue];
		if (tns == null) {
			return TermNatures.NULL;
		}
		return tns;
	}

	public static void main(String[] args) {
		GetWords gwi = new GetWordsImpl();
		gwi.setStr("井冈山：党建信息化服务新平台");
		String temp = null;
		while ((temp = gwi.allWords()) != null) {
			System.out.println(temp+ gwi.getOffe());
		}
	}

	@Override
	public int getOffe() {
		// TODO Auto-generated method stub
		return offe;
	}

}