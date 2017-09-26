package org.ansj.domain;

import org.ansj.library.NatureLibrary;

import java.io.Serializable;

public class NumNatureAttr implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final NumNatureAttr NULL = new NumNatureAttr(false, false);

	public static final NumNatureAttr NUM = new NumNatureAttr(true, false);

	public Nature nature;

	// 是有可能是一个数字
	public boolean num;

	public boolean qua;


	private NumNatureAttr(boolean num, boolean qua) {
		this.num = num;
		this.qua = qua;
		this.nature = NatureLibrary.getNature("mq");
	}

	public NumNatureAttr(boolean num, boolean qua, String natureStr) {
		this.num = num;
		this.qua = qua;
		this.nature = NatureLibrary.getNature(natureStr);
	}
}
