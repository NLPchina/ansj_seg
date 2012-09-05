package org.ansj.splitWord;

public interface GetWords {
	/**
	 * 正向最大取词
	 * @param str 传入的需要取词的句子
	 * @return
	 */
//	public String maxFrontWords() ;
	/**
	 * 正向最小匹配取词
	 * @param str 传入的需要取词的句子
	 * @return 返还取得的一个词
	 */
//	public String minFrontWords() ;
	/**
	 * 逆向最大匹配标记
	 * @param str 传入的需要分词的句子
	 * @return 返还分完词后的句子
	 */
//	public String maxConverseWords(String str) ;
	/**
	 * 逆向最小匹配标记
	 * @param str 传入的需要分词的句子
	 * @return 返还分完词后的句子
	 */
//	public String minConverseWords(String str) ;
	/**
	 * 全文全词全匹配
	 * @param str 传入的需要分词的句子
	 * @return 返还分完词后的句子
	 */
	public String allWords() ;
	/**
	 * 同一个对象传入词语
	 * @param temp 传入的句子
	 */
	public void setStr(String temp);
	
	public int getOffe() ;
}
