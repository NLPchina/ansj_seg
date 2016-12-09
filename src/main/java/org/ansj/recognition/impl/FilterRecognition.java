package org.ansj.recognition.impl;

import java.util.Collection;
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
 * 
 * @author Ansj
 *
 */
public class FilterRecognition implements Recognition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7041503137429986566L;

	private Set<String> filter = new HashSet<String>();

	private Set<String> natureFilter = new HashSet<String>();

	private Set<Pattern> regexList = new HashSet<Pattern>();

	/**
	 * 批量增加停用词
	 * 
	 * @param filterWords
	 * @return
	 */
	public FilterRecognition insertStopWords(Collection<String> filterWords) {
		filter.addAll(filterWords);
		return this;
	}

	/**
	 * 批量增加停用词
	 * 
	 * @param filterWords
	 * @return
	 */
	public FilterRecognition insertStopWords(String... filterWords) {
		for (String words : filterWords) {
			filter.add(words);
		}
		return this;
	}

	/**
	 * 批量增加停用词性 比如 增加nr 后.人名将不在结果中
	 * 
	 * @param filterWords
	 */
	public void insertStopNatures(String... filterNatures) {
		for (String natureStr : filterNatures) {
			natureFilter.add(natureStr);
		}
	}

	/**
	 * 增加正则表达式过滤
	 * 
	 * @param regex
	 */
	public void insertStopRegexes(String... regexes) {
		for (String regex : regexes) {
			regexList.add(Pattern.compile(regex));
		}

	}

	@Override
	public void recognition(Result result) {
		List<Term> list = result.getTerms();

		Iterator<Term> iterator = list.iterator();

		while (iterator.hasNext()) {
			Term term = iterator.next();
			if (filter(term)) {
				iterator.remove();
			}
		}

	}

	/**
	 * 判断一个词语是否停用..
	 * 
	 * @param term
	 * @return
	 */
	public boolean filter(Term term) {
		if (filter.size() > 0 && (filter.contains(term.getName()))) {
			return true;
		}

		if (natureFilter.size() > 0 && (natureFilter.contains(term.natrue().natureStr))) {
			return true;
		}

		if (regexList.size() > 0) {
			for (Pattern stopwordPattern : regexList) {
				if (stopwordPattern.matcher(term.getName()).matches()) {
					return true;
				}
			}
		}

		return false;
	}

}
