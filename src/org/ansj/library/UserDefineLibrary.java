package org.ansj.library;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import love.cq.domain.Forest;
import love.cq.domain.Value;
import love.cq.library.Library;
import love.cq.splitWord.GetWord;
import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

import org.ansj.util.MyStaticValue;

public class UserDefineLibrary {
	public static Forest FOREST = null;
	private static final String[] PARAMER = { "userDefine", "1000" };
	
	public static void main(String[] args) throws Exception {
		List<Value> values = new ArrayList<Value>() ;
		values.add(new Value("安全措施", "userDefine","1000")) ;
		Forest makeForest = Library.makeForest(values) ;
		GetWord word = makeForest.getWord("安全措施") ;
		
		System.out.println(word.getFrontWords());
	}

	static {
		try {

			long start = System.currentTimeMillis();
			FOREST = new Forest();

			// 先加载系统内置补充词典
			BufferedReader br = MyStaticValue.getUserDefineReader();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp) || InitDictionary.isInSystemDic(temp.split("\t")[0])) {
					continue;
				} else {
					Library.insertWord(FOREST, temp);
				}
			}
			// 如果系统设置了用户词典.那么..呵呵
			temp = MyStaticValue.userDefinePath;
			// 加载用户自定义词典
			Value value = null;
			String[] strs = null;
			if ((temp != null || (temp = MyStaticValue.rb.getString("userLibrary")) != null) && new File(temp).isFile()) {
				br = IOUtil.getReader(temp, "UTF-8");
				while ((temp = br.readLine()) != null) {
					if (StringUtil.isBlank(temp)) {
						continue;
					} else {
						strs = temp.split("\t");
						if (strs.length != 3) {
							value = new Value(strs[0], PARAMER);
						} else {
							value = new Value(strs[0], strs[1], strs[2]);
						}

						if (!InitDictionary.isInSystemDic(value.getKeyword())) {
							Library.insertWord(FOREST, value);
						}
					}
				}
			} else {
				System.err.println("用户自定义词典:" + temp + ", 没有这个文件!");
			}
			System.out.println("加载用户自定义词典完成用时:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("加载用户自定义词典加载失败:");
		}
	}

	/**
	 * 关键词增加
	 * 
	 * @param keyWord
	 *            所要增加的关键词
	 * @param nature
	 *            关键词的词性
	 * @param freq
	 *            关键词的词频
	 */
	public static void insertWord(String keyword, String nature, int freq) {
		String[] paramers = new String[2];
		paramers[0] = nature;
		paramers[1] = String.valueOf(freq);
		Value value = new Value(keyword, paramers);
		Library.insertWord(FOREST, value);
	}

	/**
	 * 删除关键词
	 */

	public static void removeWord(String word) {
		Library.removeWord(FOREST, word);
	}
}
