package org.ansj.domain;

import org.ansj.library.NatureLibrary;

import java.io.Serializable;

/**
 * 一个词里面会有一些词性
 * 
 * @author ansj
 */
public class TermNature implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5538058744208591381L;
	/**
	 * 系统内置的几个
	 */
	public static final TermNature M = new TermNature("m", 1);
	public static final TermNature EN = new TermNature("en", 1);
	public static final TermNature BEGIN = new TermNature("始##始", 1);
	public static final TermNature END = new TermNature("末##末", 1);
	public static final TermNature USER_DEFINE = new TermNature("userDefine", 1);
	public static final TermNature NR = new TermNature("nr", 1);
	public static final TermNature NT = new TermNature("nt", 1);
	public static final TermNature NS = new TermNature("ns", 1);
	public static final TermNature NW = new TermNature("nw", 1);
	public static final TermNature NRF = new TermNature("nrf", 1);
	public static final TermNature NULL = new TermNature("null", 1);

	public Nature nature;

	public int frequency;

	public TermNature(String natureStr, int frequency) {
		this.nature = NatureLibrary.getNature(natureStr);
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return nature.natureStr + "/" + frequency;
	}
}
