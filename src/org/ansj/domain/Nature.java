package org.ansj.domain;

/**
 * 这里面封装了一些基本的词性.
 * 
 * @author ansj
 * 
 */
public class Nature {
	public final String natureStr;
	public final int index;
	public final int natureIndex;
	public final int allFrequency ;

	public Nature(String natureStr, int index, int natureIndex , int allFrequency) {
		this.natureStr = natureStr;
		this.index = index;
		this.natureIndex = natureIndex;
		this.allFrequency = allFrequency ;
	}

	public String toString() {
		return natureStr + ":" + index + ":" + natureIndex;
	}
}