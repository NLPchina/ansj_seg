package org.ansj.app.summary;

/**
 * 自动摘要,关键词标红
 * 
 * @author ansj
 * 
 */
public class Summary {

	private String beginTag, endTag;

	public Summary() {

	}

	public Summary(String beginTag, String endTag) {
		this.beginTag = beginTag;
		this.endTag = endTag;
	}

	/**
	 * 传入标题正文计算摘要
	 * 
	 * @param title
	 * @param content
	 * @return
	 */
	public String tagAndSummary(String query, String content) {
		return null;
	}

	public static String summary() {
		return null;
	}
}
