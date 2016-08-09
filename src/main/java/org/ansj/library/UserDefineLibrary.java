package org.ansj.library;

import static org.ansj.util.MyStaticValue.LIBRARYLOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 用户自定义词典操作类
 * 
 * @author ansj
 */
public class UserDefineLibrary {

	public static final String DEFAULT_NATURE = "userDefine";

	public static final Integer DEFAULT_FREQ = 1000;

	public static final String DEFAULT_FREQ_STR = "1000";

	public static Forest FOREST = null;

	public static Forest ambiguityForest = null;

	static {
		initUserLibrary();
		initAmbiguityLibrary();
	}

	/**
	 * 关键词增加
	 * 
	 * @param keyword
	 *            所要增加的关键词
	 * @param nature
	 *            关键词的词性
	 * @param freq
	 *            关键词的词频
	 */
	public static void insertWord(String keyword, String nature, int freq) {
		if (FOREST == null) {
			FOREST = new Forest();
		}
		String[] paramers = new String[2];
		paramers[0] = nature;
		paramers[1] = String.valueOf(freq);
		Value value = new Value(keyword, paramers);
		Library.insertWord(FOREST, value);
	}

	/**
	 * 增加关键词
	 * 
	 * @param keyword
	 */
	public static void insertWord(String keyword) {
		insertWord(keyword, DEFAULT_NATURE, DEFAULT_FREQ);
	}

	/**
	 * 加载纠正词典
	 */
	private static void initAmbiguityLibrary() {
		String ambiguityLibrary = MyStaticValue.ambiguityLibrary;
		if (StringUtil.isBlank(ambiguityLibrary)) {
			LIBRARYLOG.warn("init ambiguity  warning :{} because : file not found or failed to read !",
					ambiguityLibrary);
			return;
		}
		ambiguityLibrary = MyStaticValue.ambiguityLibrary;
		ambiguityForest = new Forest();
		try (BufferedReader br = IOUtil.getReader(ambiguityLibrary, "utf-8")) {
			String temp = null;
			boolean first = true;
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				}
				if (first) {
					temp = StringUtil.trim(temp);
				}
				String[] split = temp.split("\t");
				StringBuilder sb = new StringBuilder();
				if (split.length % 2 != 0) {
					LIBRARYLOG.error("init ambiguity  error in line :" + temp + " format err !");
				}
				for (int i = 0; i < split.length; i += 2) {
					sb.append(split[i]);
				}
				ambiguityForest.addBranch(sb.toString(), split);
			}
			LIBRARYLOG.info("init ambiguityLibrary ok!");
		} catch (FileNotFoundException e) {
			LIBRARYLOG.warn("init ambiguity  error :{} because : not find that file or can not found!",
					new File(ambiguityLibrary).getAbsolutePath());
		} catch (UnsupportedEncodingException e) {
			LIBRARYLOG.warn("不支持的编码", e);
		} catch (IOException e) {
			LIBRARYLOG.warn("init ambiguity  error :{} because : not find that file or can not to read!",
					new File(ambiguityLibrary).getAbsolutePath());
		}
	}

	/**
	 * 加载用户自定义词典和补充词典
	 */
	private static void initUserLibrary() {
		FOREST = MyStaticValue.getDicForest();
		// 加载用户自定义词典
	}

	// 单个文件加载词典
	public static void loadFile(Forest forest, File file) {
		if (!file.canRead()) {
			LIBRARYLOG.warn("file in path " + file.getAbsolutePath() + " can not to read!");
			return;
		}
		String temp = null;
		String[] strs = null;
		Value value = null;
		try (BufferedReader br = IOUtil.getReader(new FileInputStream(file), "UTF-8")) {
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				} else {
					strs = temp.split("\t");
					strs[0] = strs[0].toLowerCase();
					// 如何核心辞典存在那么就放弃
					if (MyStaticValue.isSkipUserDefine && DATDictionary.getId(strs[0]) > 0) {
						continue;
					}
					if (strs.length != 3) {
						value = new Value(strs[0], DEFAULT_NATURE, DEFAULT_FREQ_STR);
					} else {
						value = new Value(strs[0], strs[1], strs[2]);
					}
					Library.insertWord(forest, value);
				}
			}
			LIBRARYLOG.info("init user userLibrary ok path is : {}", file.getAbsolutePath());
		} catch (UnsupportedEncodingException e) {
			LIBRARYLOG.warn("不支持的编码", e);
		} catch (IOException e) {
			LIBRARYLOG.warn("IO异常", e);
		}
	}

	/**
	 * 加载词典,传入一本词典的路径.或者目录.词典后缀必须为.dic 按文件名称顺序加载
	 */
	public static void loadLibrary(Forest forest, String path) {
		// 加载用户自定义词典
		File file;
		if (path != null) {
			file = new File(path);
			if (!file.exists()) {
				// Try load from classpath
				URL url = UserDefineLibrary.class.getClassLoader().getResource(path);
				if (url != null) {
					file = new File(url.getPath());
				}
			}
			if (!file.canRead() || file.isHidden()) {
				LIBRARYLOG.warn("init userLibrary  warning :{} because : file not found or failed to read !",
						file.getAbsolutePath());
				return;
			}
			if (file.isFile()) {
				loadFile(forest, file);
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files == null) {
					return;
				}
				List<File> fileList = Arrays.asList(files);
				Collections.sort(fileList, new Comparator<File>() {
					@Override
					public int compare(File target, File source) {
						return target.getName().compareTo(source.getName());
					}
				});
				for (File dicFile : fileList) {
					if (dicFile.getName().trim().endsWith(".dic")) {
						loadFile(forest, dicFile);
					}
				}
			} else {
				LIBRARYLOG.warn("init user library  error :{} because : not find that file !", file.getAbsolutePath());
			}
		}
	}

	/**
	 * 删除关键词
	 */
	public static void removeWord(String word) {
		Library.removeWord(FOREST, word);
	}

	public static String[] getParams(String word) {
		return getParams(FOREST, word);
	}

	public static String[] getParams(Forest forest, String word) {
		SmartForest<String[]> temp = forest;
		for (int i = 0; i < word.length(); i++) {
			temp = temp.get(word.charAt(i));
			if (temp == null) {
				return null;
			}
		}
		if (temp.getStatus() > 1) {
			return temp.getParam();
		} else {
			return null;
		}
	}

	public static boolean contains(String word) {
		return getParams(word) != null;
	}

	/**
	 * 将用户自定义词典清空
	 */
	public static void clear() {
		FOREST.clear();
	}

}
