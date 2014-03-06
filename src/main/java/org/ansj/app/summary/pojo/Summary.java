package org.ansj.app.summary.pojo;

import java.util.List;

/**
 * 摘要结构体封装
 * 
 * @author ansj
 * 
 */
public class Summary {

	/**
	 * 关键词
	 */
	private List<String> keyWords = null;

	/**
	 * 摘要
	 */
	private String summary;

	public Summary(List<String> keyWords, String summary) {
		this.keyWords = keyWords;
		this.summary = summary;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public String getSummary() {
		return summary;
	}

}
