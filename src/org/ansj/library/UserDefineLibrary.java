package org.ansj.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import love.cq.domain.Forest;
import love.cq.domain.Value;
import love.cq.library.Library;
import love.cq.splitWord.GetWord;
import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

import org.ansj.util.MyStaticValue;

/**
 * 用户自定义词典操作类
 * 
 * @author ansj
 */
public class UserDefineLibrary {

	public static Forest FOREST = null;

	private static final String[] PARAMER = { "userDefine", "1000" };

	public static void main(String[] args) throws Exception {
		List<Value> values = new ArrayList<Value>();
		values.add(new Value("安全措施", "userDefine", "1000"));
		Forest makeForest = Library.makeForest(values);
		GetWord word = FOREST.getWord("北京理工大学办事处");

		System.out.println(word.getFrontWords());
		UserDefineLibrary.clear();

		word = FOREST.getWord("达到");

		System.out.println(word.getFrontWords());
	}

	static {
		try {
			long start = System.currentTimeMillis();
			FOREST = new Forest();
			// 先加载系统内置补充词典
			initSystemLibrary(FOREST);
			loadLibrary(FOREST, MyStaticValue.userDefinePath);
			System.out.println("init user library ok use time :" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private static void initSystemLibrary(Forest FOREST) {
		// TODO Auto-generated method stub
		String temp = null;
		BufferedReader br = null;

		br = MyStaticValue.getSystemLibraryReader();

		try {
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				} else {
					Library.insertWord(FOREST, temp);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.close(br);
		}
	}

	// 单个文件价值词典
	public static void loadFile(Forest forest, File file) {
		// TODO Auto-generated method stub
		if (!file.canRead()) {
			System.err.println("file in path " + file.getAbsolutePath() + " can not to read!");
			return;
		}
		String temp = null;
		BufferedReader br = null;
		String[] strs = null;
		Value value = null;
		try {
			br = IOUtil.getReader(new FileInputStream(file), "UTF-8");
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
					Library.insertWord(forest, value);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtil.close(br);
			br = null;
		}
	}

	/**
	 * 用户自定义自己的词典,生成
	 * @param isSystem 是否加载系统词典
	 * @param libraryPaths 词典路径,可以是目录,也可以是具体的文件.如果是目录.只加载后缀为dic的文件
	 * @return 返回的词典结构.
	 */
	public static Forest makeUserDefineForest(boolean isSystem, String... libraryPaths) {
		Forest forest = new Forest();
		if (isSystem) {
			initSystemLibrary(forest);
		}
		for (String path : libraryPaths) {
			loadLibrary(forest,path) ;
		}
		return forest;
	}

	/**
	 * 加载词典,传入一本词典的路径.或者目录.词典后缀必须为.dic
	 */
	public static void loadLibrary(Forest forest, String temp) {
		// 加载用户自定义词典
		File file = null;
		if ((temp != null || (temp = MyStaticValue.rb.getString("userLibrary")) != null)) {
			file = new File(temp);
			if (file.isFile()) {
				loadFile(forest,file);
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (file.getName().trim().endsWith(".dic")) {
						loadFile(forest, files[i]);
					}
				}
			} else {
				System.err.println("init user library  error :" + temp + " because : not find that file !");
			}
		}
	}

	/**
	 * 删除关键词
	 */
	public static void removeWord(String word) {
		Library.removeWord(FOREST, word);
	}

	/**
	 * 将用户自定义词典清空
	 */
	public static void clear() {
		FOREST.clear();
	}

}
