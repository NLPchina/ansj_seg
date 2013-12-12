package org.ansj.util;

public class MatrixUtil {

	/**
	 * 向量求和
	 * 
	 * @param dbs
	 * @return
	 */
	public static double sum(double[] dbs) {
		double value = 0;
		for (double d : dbs) {
			value += d;
		}
		return value;
	}

	public static int sum(int[] dbs) {
		int value = 0;
		for (int d : dbs) {
			value += d;
		}
		return value;
	}

	public static double sum(double[][] w) {
		// TODO Auto-generated method stub
		double value = 0;
		for (double[] dbs : w) {
			value += sum(dbs);
		}
		return value;
	}

	public static void dot(double[] feature, double[] feature1) {
		if (feature1 == null) {
			return;
		}
		for (int i = 0; i < feature1.length; i++) {
			feature[i] += feature1[i];
		}
	}
}
