package org.ansj.app.crf.pojo;

import java.util.Arrays;

public class Feature {

	public double value = 0;

	public double[][] w;

	public int tagNum;

	public Feature(int featureNum, int tagNum) {
		w = new double[featureNum][0];
		this.tagNum = tagNum;
	}

	public Feature(double[][] w) {
		this.w = w;
	}

	public void update(int fIndex, int sta, double step) {
		value += step;
		if (w[fIndex].length == 0) {
			w[fIndex] = new double[tagNum];
		}
		w[fIndex][sta] += step;
	};

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (double[] ints : w) {
			sb.append(Arrays.toString(ints));
			sb.append("\n");
		}
		return sb.toString();
	}

}