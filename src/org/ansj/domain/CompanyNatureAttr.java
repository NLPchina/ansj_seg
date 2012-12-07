package org.ansj.domain;

/**
 * 地址名的前缀,所有元素.开放为public是为了省事.而不是为了好修改
 * 
 * @author ansj
 * 
 */
public class CompanyNatureAttr {

	private static final double ALL = 52399292;

	public static double BEGIN = 3186 / ALL;
	public static double END = 2800 / ALL;

	public static final CompanyNatureAttr NULL = new CompanyNatureAttr(0, 0, 0, 0, 0, 0);

	public CompanyNatureAttr(int p, int b, int m, int e, int s, int allFreq) {
		allFreq++;
		this.p = p;
		this.b = b;
		this.m = m;
		this.e = e;
		this.s = s;
		this.allFreq = allFreq;

		double all = p + b + m + e + s + allFreq + 1;

		pb = (p / all) * Math.log(1 - this.p / all);
		bb = (b / all) * Math.log(1 - this.b / all);
		mb = (m / all) * Math.log(1 - this.m / all);
		eb = (e / all) * Math.log(1 - this.e / all);
		sb = (s / all) * Math.log(1 - this.s / all);

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

	// 前缀idf
	public double pb;
	// b首字词idf
	public double bb;
	// 中间部分idf
	public double mb;
	// 结尾部分idf
	public double eb;
	// 后缀idf
	public double sb;
	// 所有词频idf
	public double allFreqb;

	@Override
	public String toString() {
		return "p" + ":" + p + "\t" + "b" + ":" + b + "\t" + "m" + ":" + m + "\t" + "e" + ":" + e + "\t" + "s" + ":" + s;
	}

}
