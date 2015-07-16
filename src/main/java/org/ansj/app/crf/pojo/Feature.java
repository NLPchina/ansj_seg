package org.ansj.app.crf.pojo;

import java.util.Arrays;

public class Feature {

	public double[][] w;

	public int tagNum;

    public double value = 0;

	public Feature(final int featureNum, final int tagNum) {
		this.w = new double[featureNum][0];
		this.tagNum = tagNum;
	}

	public Feature(final double[][] w) {
		this.w = w;
	}

	public void update(final int fIndex, final int sta, final double step) {
		this.value += step;
		if (this.w[fIndex].length == 0) {
			this.w[fIndex] = new double[this.tagNum];
		}
		this.w[fIndex][sta] += step;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final double[] ints : this.w) {
			sb.append(Arrays.toString(ints)).append("\n");
		}
		return sb.toString();
	}
}