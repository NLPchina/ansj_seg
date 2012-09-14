package org.ansj.util.template;

public class TireRegex {

	public static final int XIEGANG = '\\';

	/**
	 * 增加正则
	 * 
	 * @param regex
	 */
	public void add(String regex) {

	}

	private void paser(String regex) {
		boolean flag = false;
		for (int i = 0; i < regex.length(); i++) {
			if (regex.charAt(i) == XIEGANG) {
				flag = true;
			} else {
				Node node = new Node() ;
				flag = false;
			}
		}
	}

}
