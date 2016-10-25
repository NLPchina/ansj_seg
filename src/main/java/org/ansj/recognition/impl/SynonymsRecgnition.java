package org.ansj.recognition.impl;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;

/**
 * 同义词功能
 * 
 * @author Ansj
 *
 */
public class SynonymsRecgnition implements Recognition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5961499108093950130L;

	private static final Log LOG = MyStaticValue.getLog();

	private static SmartForest<List<String>> SYS_SYNONYMS = null;

	private SmartForest<List<String>> synonyms = new SmartForest<>();

	public SmartForest<List<String>> initLibrary(String synonymsLibrary) {

		if (synonymsLibrary == null || !new File(synonymsLibrary).exists()) {
			MyStaticValue.LIBRARYLOG.warn(synonymsLibrary + " not exists so set syn to empty!");
		} else {
			try (BufferedReader reader = IOUtil.getReader(synonymsLibrary, IOUtil.UTF8)) {
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					if (StringUtil.isBlank(temp)) {
						continue;
					}
					String[] split = temp.split("\t");

					List<String> list = new ArrayList<>();
					for (String word : split) {
						if (StringUtil.isBlank(word)) {
							continue;
						}
						list.add(word);
					}

					if (split.length <= 1) {
						MyStaticValue.LIBRARYLOG.warn(temp + " in synonymsLibrary not in to library !");
						continue;
					}

					for (int i = 0; i < split.length; i++) {
						synonyms.add(split[i], list);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LOG.info("init library synonymsLibrary ok from " + new File(synonymsLibrary).getAbsolutePath());
		return synonyms;
	}

	public SynonymsRecgnition() {
		if (SYS_SYNONYMS == null) {
			synchronized (SynonymsRecgnition.class) {
				if (SYS_SYNONYMS == null) {
					SYS_SYNONYMS = initLibrary(MyStaticValue.synonymsLibrary);
				}
			}
		}
		synonyms = SYS_SYNONYMS;
	}

	public SynonymsRecgnition(String synonymsLibrary) {
		initLibrary(synonymsLibrary);
	}

	/**
	 * 覆盖更新同义词 [中国, 中华, 我国] -> replace([中国,华夏]) -> [中国,华夏]
	 * 
	 * @param words
	 */
	public void insert(String[] words) {

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

	@Override
	public void recognition(Result result) {
		for (Term term : result) {
			SmartForest<List<String>> branch = synonyms.getBranch(term.getName());
			if (branch != null && branch.getStatus() > 1) {
				List<String> syns = branch.getParam();
				if (syns != null) {
					term.setSynonyms(syns);
				}
			}
		}
	}

}
