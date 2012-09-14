package org.ansj.util.template;

public class Node {
	//匹配类型
	private char type ;
	//出现最多次数
	private int moreCount ;
	//出现最少次数
	private int lessCount ;
	
	public static void main(String[] args) {
		System.out.println("\\m毛\\m");
		System.out.println("\\m{2,4}年\\m{1,2}月\\m{1,3}日");
	}
}
