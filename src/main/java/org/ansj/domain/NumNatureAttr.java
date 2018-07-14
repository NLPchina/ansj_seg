package org.ansj.domain;

import org.ansj.library.NatureLibrary;

import java.io.Serializable;

public class NumNatureAttr implements Serializable {

	public static final NumNatureAttr NULL = new NumNatureAttr(false, false);

	public static final NumNatureAttr NUM = new NumNatureAttr(true, false);

	public Nature nature;

	// 是有可能是一个数字
	private boolean num;

	private boolean qua;


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

	public boolean isNum() {
		return num;
	}

	public boolean isQua() {
		return qua;
	}

	public void setNum(boolean num) {
		this.num = num;
	}

	public void setQua(boolean qua) {
		this.qua = qua;
	}

	public Nature getNature() {
		return nature;
	}
}
