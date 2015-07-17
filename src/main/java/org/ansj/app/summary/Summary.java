package org.ansj.app.summary;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ansj.app.keyword.Keyword;

/**
 * 摘要结构体封装
 * 
 * @author ansj
 * 
 */
//@AllArgsConstructor
@Getter
public class Summary {

	/**
	 * 关键词
	 */
	private final List<Keyword> keyWords;

	/**
	 * 摘要
	 */
	private final String summary;

	public Summary(final List<Keyword> keyWords, final String summary) {
		this.keyWords = keyWords;
		this.summary = summary;
	}
}
