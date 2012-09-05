package org.ansj.util;

public class ObjectBean {
	public static int getInt(String str , int def){
		try {
			return Integer.parseInt(str) ;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return def ;
		}
	}
}
