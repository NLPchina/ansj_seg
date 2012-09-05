package org.ansj.util;

import java.util.ResourceBundle;

public class MyStaticValue {
	public final static ResourceBundle rb = ResourceBundle.getBundle("library") ; 
	public static final String ENGLISH = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJK" +
		"LMNOPQRSTUVWXYZ'ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢ" +
		"ＣＤＥＦＧＨＩＪＫＭＬＮＯＰＱＲＳＴＵＶＷＸＹＺ" ;
	public static final String NUMBER = "0123456789０１２３４５６７８９.%" ;
	public static final String NUMBERALLSTR = "0123456789０１２３４５６７８９零一二三四五六七八九十百千万亿兆零壹贰叁肆伍陆柒捌玖拾佰仟." ;
	public static final char[] NUMBERALL = getSortChars(NUMBERALLSTR) ;
	public static final String NAMESTOPSTR ="不也了仍从以使则却又及对就并很或把是的着给而被让说.,:\";，。 ：“、" ;
	public static final char[] NAMESTOPALL = getSortChars(NAMESTOPSTR) ;
	public static char[] getSortChars(String str) {
		char[] chars = str.toCharArray() ;
		java.util.Arrays.sort(chars) ;
		return chars ;
	}
}
