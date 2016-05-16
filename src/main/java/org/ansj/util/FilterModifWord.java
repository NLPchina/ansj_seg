package org.ansj.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;

/*
 * 停用词过滤,修正词性到用户词性.
 */
public class FilterModifWord {

	private static Set<String> FILTER = new HashSet<String>();

	private static String TAG = "#";

	private static boolean isTag = false;
	// 停用词正则表达式
	private static Pattern stopwordPattern;

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

	public static void insertStopRegex(String regex) {
		stopwordPattern = Pattern.compile(regex);
	}

	public static void removeStopRegex() {
		stopwordPattern = null;
	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all) {
		List<Term> result = new ArrayList<Term>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || (isTag && FILTER.contains(TAG + term.natrue().natureStr)))) {
					continue;
				}
				// 添加对正则停用词的支持
				if ((stopwordPattern != null) && stopwordPattern.matcher(term.getName()).matches()) {
					continue;
				}
				String[] params = UserDefineLibrary.getParams(term.getName());
				if (params != null) {
					term.setNature(new Nature(params[0]));
				}
				result.add(term);
			}
		} catch (Exception e) {
			MyStaticValue.LIBRARYLOG.warn("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all, Forest... forests) {
		List<Term> result = new ArrayList<Term>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || FILTER.contains(TAG + term.natrue().natureStr))) {
					continue;
				}
				// 添加对正则停用词的支持
				if ((stopwordPattern != null) && stopwordPattern.matcher(term.getName()).matches()) {
					continue;
				}

				if (forests == null) {
					if (UserDefineLibrary.FOREST != null) {
						forests = new Forest[] { UserDefineLibrary.FOREST };
					} else {
						continue;
					}
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
			MyStaticValue.LIBRARYLOG.warn("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}

	/*
	 * 修正词性
	 */
	public static List<Term> updateNature(List<Term> all, Forest... forests) {

		if (forests == null) {
			if (UserDefineLibrary.FOREST != null) {
				forests = new Forest[] { UserDefineLibrary.FOREST };
			} else {
				return all;
			}
		}

		List<Term> result = new ArrayList<Term>();
		
		for (Term term : all) {

			// 添加对正则停用词的支持
			if ((stopwordPattern != null) && stopwordPattern.matcher(term.getName()).matches()) {
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
		
		return result;
	}
}
