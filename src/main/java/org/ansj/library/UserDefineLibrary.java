package org.ansj.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 用户自定义词典操作类
 *
 * @author ansj
 */
public class UserDefineLibrary {

	
	/**
	 * 覆盖更新同义词 [中国, 中华, 我国] -> replace([中国,华夏]) -> [中国,华夏]
	 * 
	 * @param words
	 */
	public void insert(String key, String[] words) {

		List<String> list = new ArrayList<>();

		for (String word : words) {
			if (StringUtil.isBlank(word)) {
				continue;
			}
			list.add(word);
		}

		if (list.size() <= 1) {
			LOG.warn(Arrays.toString(words) + " not have any change because it less than 2 word");
			return;
		}

		Set<String> set = findAllWords(words);

		for (String word : list) {
			set.remove(word);
			synonyms.add(word, list);
		}

		for (String word : set) { //删除所有
			synonyms.remove(word);
			synonyms.getBranch(word).setParam(null);
		}

	}

	private Set<String> findAllWords(String[] words) {
		Set<String> set = new HashSet<>();

		for (String word : words) {
			SmartForest<List<String>> branch = synonyms.getBranch(word);
			if (branch != null) {
				List<String> params = branch.getParam();
				if (params != null) {
					set.addAll(params);
				}
			}
		}
		return set;
	}

	/**
	 * 合并更新同义词 覆盖更新同义词 [中国, 中华, 我国] -> append([中国,华夏]) -> [中国, 中华, 我国 , 华夏]
	 * 
	 * @param words
	 */
	public void append(String[] words) {

		Set<String> set = new HashSet<>();

		for (String word : words) {
			if (StringUtil.isBlank(word)) {
				continue;
			}
			set.add(word);
		}

		if (set.size() <= 1) {
			LOG.warn(Arrays.toString(words) + " not have any change because it less than 2 word");
			return;
		}

		set.addAll(findAllWords(words));

		List<String> list = new ArrayList<>(set);

		for (String word : list) {
			synonyms.addBranch(word, list);
		}
	}

	/**
	 * 从同义词组中删除掉一个词 [中国, 中华, 我国] -> remove(我国) -> [中国, 中华]
	 * 
	 * @param words
	 */
	public void remove(String word) {

		SmartForest<List<String>> branch = synonyms.getBranch(word);

		if (branch == null || branch.getStatus() < 2) {
			return;
		}

		List<String> params = branch.getParam();

		synonyms.remove(word);
		branch.setParam(null);
		params.remove(word);

		if (params.size() == 1) { //如果是1 个也删除
			synonyms.remove(params.get(0));
			params.remove(0);
		} else {
			params.remove(word);
		}
	}
}
