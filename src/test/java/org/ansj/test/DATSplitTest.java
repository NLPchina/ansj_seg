package org.ansj.test;

import org.ansj.splitWord.impl.GetWordsImpl;

public class DATSplitTest {
	public static void main(String[] args) {
		GetWordsImpl gwi = new GetWordsImpl();
		gwi.setStr("程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪");
		String temp = null ;
		while((temp = gwi.allWords())!=null){
			System.out.println(temp);
		}
	}
}
