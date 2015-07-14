package org.ansj.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;

/*
 * 停用词过滤,修正词性到用户词性.
 * TODO 改掉static
 */
public class FilterModifWord {

	private static final Set<String> FILTER = new HashSet<>();

	private static final String TAG = "#";

	private static boolean isTag = false;

	public static void insertStopWords(List<String> filterWords) {
		FILTER.addAll(filterWords);
	}

	public static void insertStopWord(String... filterWord) {
		for (String word : filterWord) {
			FILTER.add(word);
		}
	}

	public static void insertStopNatures(String... filterNatures) {
		isTag = true;
		for (String natureStr : filterNatures) {
			FILTER.add(TAG + natureStr);
		}

	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all) {
		final List<Term> result = new ArrayList<>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || (isTag && FILTER.contains(TAG + term.natrue().natureStr)))) {
					continue;
				}
				String[] params = UserDefineLibrary.getInstance().getParams(term.getName());
				if (params != null) {
					term.setNature(new Nature(params[0]));
				}
				result.add(term);
			}
		} catch (Exception e) {
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(final List<Term> all, final Forest... forests) {
		final List<Term> result = new ArrayList<>();
		try {
			for (final Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || FILTER.contains(TAG + term.natrue().natureStr))) {
					continue;
				}
				for (Forest forest : forests) {
					String[] params = UserDefineLibrary.getParams(forest, term.getName());
					if (params != null) {
						term.setNature(new Nature(params[0]));
					}
				}
				result.add(term);
			}
		} catch (Exception e) {
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}
}
