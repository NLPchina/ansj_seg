package org.ansj.recognition.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;

/**
 * 对结果增加过滤,支持词性过滤,和词语过滤.
 * @author Ansj
 *
 */
public class FilterRecognition implements Recognition {

	private Set<String> filter = new HashSet<String>();

	private Set<String> natureFilter = new HashSet<String>();

	private List<Pattern> regexList = new ArrayList<Pattern>();

	/**
	 * 批量增加停用词
	 * @param filterWords
	 */
	public void insertStopWords(List<String> filterWords) {
		filter.addAll(filterWords);
	}

	/**
	 * 批量增加停用词
	 * @param filterWords
	 */
	public void insertStopWord(String... filterWord) {
		for (String word : filterWord) {
			filter.add(word);
		}
	}

	/**
	 * 批量增加停用词性 比如 增加nr 后.人名将不在结果中
	 * @param filterWords
	 */
	public void insertStopNatures(String... filterNatures) {
		for (String natureStr : filterNatures) {
			natureFilter.add(natureStr);
		}
	}

	/**
	 * 增加正则表达式过滤
	 * @param regex
	 */
	public void insertStopRegex(String regex) {
		regexList.add(Pattern.compile(regex));
	}

	@Override
	public void recognition(Result result) {
		List<Term> list = result.getTerms();

		f: for (Iterator<Term> iterator = list.iterator(); iterator.hasNext();) {
			Term term = iterator.next();
			if (filter.size() > 0 && (filter.contains(term.getName()))) {
				iterator.remove();
				continue f;
			}

			if (natureFilter.size() > 0 && (natureFilter.contains(term.natrue().natureStr))) {
				iterator.remove();
				continue f;
			}

			if (regexList.size() > 0) {
				for (Pattern stopwordPattern : regexList) {
					if (stopwordPattern.matcher(term.getName()).matches()) {
						iterator.remove();
						continue f;
					}
				}
			}

		}

	}

}
