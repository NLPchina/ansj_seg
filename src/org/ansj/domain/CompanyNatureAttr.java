package org.ansj.domain;

import java.util.Random;

/**
 * 地址名的前缀,所有元素.开放为public是为了省事.而不是为了好修改
 * 
 * @author ansj
 * 
 */
public class CompanyNatureAttr {

	private static final double ALL = 52399292;
	
	public static double BEGIN = 3186/ALL;
	public static double END = 2800/ALL;

	public static final CompanyNatureAttr NULL = new CompanyNatureAttr(0, 0, 0, 0, 0, 0);

	public CompanyNatureAttr(int p, int b, int m, int e, int s, int allFreq) {
		this.p = p;
		this.b = b;
		this.m = m;
		this.e = e;
		this.s = s;
		this.allFreq = allFreq;

		double all = p + b + m + e + s + allFreq + 1;

		pb = p / all;
		bb = b / all;
		mb = m / all;
		eb = e / all;
		sb = s / all;

	}

	// 前缀
	public int p;
	// b首字词
	public int b;
	// 中间部分
	public int m;
	// 结尾部分
	public int e;
	// 后缀
	public int s;
	// 所有词频
	public int allFreq;

	// 前缀
	public double pb;
	// b首字词
	public double bb;
	// 中间部分
	public double mb;
	// 结尾部分
	public double eb;
	// 后缀
	public double sb;
	// 所有词频
	public double allFreqb;

	public String toString() {
		return "p" + ":" + p + "\t" + "b" + ":" + b + "\t" + "m" + ":" + m + "\t" + "e" + ":" + e + "\t" + "s" + ":" + s;
	}

	public static void main(String[] args) {
		int id = 10;
		int[] ints = new int[800];
		for (int i = 0; i < 800; i++) {
			ints[i] = new Random(Integer.MAX_VALUE).nextInt();
		}

		boolean flag = false;
		long start = System.currentTimeMillis();
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			for (int j = 0; j < ints.length; j++) {
				flag = ints[j] == i;
			}
		}
		System.out.println(System.currentTimeMillis() - start);
	}
}
