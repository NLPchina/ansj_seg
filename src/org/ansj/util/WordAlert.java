package org.ansj.util;

public class WordAlert {
	public static void main(String[] args) {
		System.out.println((int)'１') ;
//		System.out.println((int)'ｂ') ;
//		System.out.println((int)'') ;
		System.out.println((int)'９') ;
		System.out.println((int)'０') ;
//		System.out.println("-----------------------------") ;
		System.out.println((int)'0') ;
//		System.out.println((int)'Ｂ') ;
//		System.out.println((int)'Ｃ') ;
//		System.out.println((int)'Ｄ') ;
//		System.out.println((int)'Ｚ') ;
//		System.out.println((int)'Z');
		String str = "１２３４５６４９８７大法师说的12323123" ;
//		long start = System.currentTimeMillis() ;
//		for (int i = 0; i < 10000000; i++) {
////			alertEnglish(str.toCharArray(),0,str.length());
//			str.toLowerCase();
//		}
//		System.out.println(System.currentTimeMillis()-start);
		System.out.println(alertNumber(str.toCharArray(),0,str.length()));;
	}
	/**
	 * 这个就是(int)'ａ'
	 */
	public static final int MIN_LOWER = 65345;
	/**
	 * 这个就是(int)'ｚ'
	 */
	public static final int MAX_LOWER = 65370;
	/**
	 * 差距进行转译需要的
	 */
	public static final int LOWER_GAP = 65248;
	/**
	 * 这个就是(int)'Ａ'
	 */
	public static final int MIN_UPPER = 65313;
	/**
	 * 这个就是(int)'Ｚ'
	 */
	public static final int MAX_UPPER = 65338;
	/**
	 * 差距进行转译需要的
	 */
	public static final int UPPER_GAP = 65216;
	/**
	 * 这个就是(int)'A'
	 */
	public static final int MIN_UPPER_E = 65;
	/**
	 * 这个就是(int)'Z'
	 */
	public static final int MAX_UPPER_E = 90;
	/**
	 * 差距进行转译需要的
	 */
	public static final int UPPER_GAP_E = -32;
	/**
	 * 这个就是(int)'0'
	 */
	public static final int MIN_UPPER_N = 65296;
	/**
	 * 这个就是(int)'９'
	 */
	public static final int MAX_UPPER_N = 65305;
	/**
	 * 差距进行转译需要的
	 */
	public static final int UPPER_GAP_N = 65248;

	
	/**
	 * 对全角的字符串,大写字母进行转译.如ｓｄｆｓｄｆ
	 * @param chars
	 * @param start
	 * @param end
	 * @return
	 */
	public static String alertEnglish(char[] chars , int start , int end){
		for (int i = start; i < start+end; i++) {
			if(chars[i]>=MIN_LOWER&&chars[i]<=MAX_LOWER){
				chars[i] = (char)(chars[i]-LOWER_GAP) ;
			}
			if(chars[i]>=MIN_UPPER&&chars[i]<=MAX_UPPER){
				chars[i] = (char)(chars[i]-UPPER_GAP) ;
			}
			if(chars[i]>=MIN_UPPER_E&&chars[i]<=MAX_UPPER_E){
				chars[i] = (char)(chars[i]-UPPER_GAP_E) ;
			}
		}
		return new String(chars,start,end) ;
	}
	
	public static String alertEnglish(String temp , int start , int end){
		char c = 0;
		StringBuilder sb = new StringBuilder() ;
		for (int i = start; i < start+end; i++) {
			c = temp.charAt(i) ;
			if(c>=MIN_LOWER&&c<=MAX_LOWER){
				sb.append((char)(c-LOWER_GAP)) ;
			}else if(c>=MIN_UPPER&&c<=MAX_UPPER){
				sb.append((char)(c-UPPER_GAP)) ;
			}else if(c>=MIN_UPPER_E&&c<=MAX_UPPER_E){
				sb.append((char)(c-UPPER_GAP_E)) ;
			}else{
				sb.append(c) ;
			}
		}
		return sb.toString() ;
	}
	public static String alertNumber(char[] chars , int start , int end){
		for (int i = start; i < start+end; i++) {
			if(chars[i]>=MIN_UPPER_N&&chars[i]<=MAX_UPPER_N){
				chars[i] = (char)(chars[i]-UPPER_GAP_N) ;
			}
		}
		return new String(chars,start,end) ;
	}
	
	public static String alertNumber(String temp , int start , int end){
		char c = 0;
		StringBuilder sb = new StringBuilder() ;
		for (int i = start; i < start+end; i++) {
			c = temp.charAt(i) ;
			if(c>=MIN_UPPER_N&&c<=MAX_UPPER_N){
				sb.append((char)(c-UPPER_GAP_N)) ;
			}else{
				sb.append(c) ;
			}
		}
		return sb.toString() ;
	}
}
