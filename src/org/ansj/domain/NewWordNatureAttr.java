package org.ansj.domain;

/**
 * 新词发现属性
 * 
 * @author ansj
 */
public class NewWordNatureAttr {
	public static final NewWordNatureAttr NULL = new NewWordNatureAttr();
	private int b;
	private int m;
	private int e;

	// 5 20
	private int all;

	private NewWordNatureAttr() {
	}

	/**
	 * 构造方法
	 * 
	 * @param b
	 * @param m
	 * @param e
	 * @param allFreq
	 */
	public NewWordNatureAttr(int b, int m, int e) {
		this.all += b + m + e;
		this.b = b;
		this.m = m;
		this.e = e;
	}

	public void updateAll(int allValue) {
		this.all += (allValue * 100);
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getE() {
		return e;
	}

	public void setE(int e) {
		this.e = e;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
	
}
