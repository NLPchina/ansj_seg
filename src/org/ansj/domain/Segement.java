package org.ansj.domain;


public class Segement {
	private String value;
	private String natureStr;
	private int offe;

	public Segement(String value, int offe) {
		this.value = value;
		this.offe = offe;
	}

	public Segement(String value, int offe, String natureStr) {
		this.value = value;
		this.offe = offe;
		this.natureStr = natureStr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNatureStr() {
		return natureStr;
	}

	public void setNatureStr(String natureStr) {
		this.natureStr = natureStr;
	}

	public int getOffe() {
		return offe;
	}

	public void setOffe(int offe) {
		this.offe = offe;
	}

	public String toString() {
		return this.value + ":" + this.offe + "/" + this.natureStr;
	}

}
