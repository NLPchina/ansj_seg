package org.ansj.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;

/*
 * 停用词过滤,修正词性到用户词性.
 */
public class FilterModifWord {

	private static Set<String> FILTER = new HashSet<String>();

	private static String TAG = "#";

	public static void insertStopWords(List<String> filterWords) {
		FILTER.addAll(filterWords);
	}

	public static void insertStopWord(String filterWord) {
		FILTER.add(filterWord);
	}

	public static void insertStopNatures(String... filterNatures) {
		for (String natureStr : filterNatures) {
			FILTER.add(TAG + natureStr);
		}

	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all) {
		List<Term> result = new ArrayList<Term>();
		try {
			for (Term term : all) {
				if (FILTER.size() > 0 && (FILTER.contains(term.getName()) || FILTER.contains(TAG + term.getNatrue().natureStr))) {
					continue;
				}
				String[] params = UserDefineLibrary.getParams(term.getName());
				if (params != null) {
					term.setNature(new Nature(params[0]));
				}
				result.add(term);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}

}
