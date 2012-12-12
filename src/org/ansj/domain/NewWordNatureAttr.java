package org.ansj.domain;

/**
 * 新词发现属性
 * 
 * @author ansj
 */
public class NewWordNatureAttr {
	public static final NewWordNatureAttr NULL = new NewWordNatureAttr();
	private double b;
	private double m;
	private double e;

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

	public void updateAll(int all) {
		this.all += all * 10;
		this.b = (b / all) * Math.log(1 - this.b / all);
		this.m = (m / all) * Math.log(1 - this.m / all);
		this.e = (e / all) * Math.log(1 - this.e / all);
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getE() {
		return e;
	}

	public void setE(double e) {
		this.e = e;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

}
