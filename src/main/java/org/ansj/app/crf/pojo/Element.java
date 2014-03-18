package org.ansj.app.crf.pojo;

import org.ansj.app.crf.Model;

public class Element {
	private static final double MIN = Integer.MIN_VALUE;

	public char name;
	private int tag = -1;
	public int len = 1;
	public String nature;

	public double[] tagScore;

	public int[] from;

	public Element(char name) {
		this.name = name;
	}

	public Element(Character name, int tag) {
		this.name = name;
		this.tag = tag;
	}

	public int getTag() {
		return tag;
	}

	public Element updateTag(int tag) {
		this.tag = tag;
		return this;
	}

	public Element updateNature(String nature) {
		this.nature = nature;
		return this;
	}

	public void len() {
		len++;
	}

	@Override
	public String toString() {
		return name + "/" + tag;
	}

	public void maxFrom(Model model, Element element) {
		if (from == null) {
			from = new int[this.tagScore.length];
		}
		double[] pTagScore = element.tagScore;
		double rate = 0;
		for (int i = 0; i < this.tagScore.length; i++) {
			double maxValue = MIN;
			for (int j = 0; j < pTagScore.length; j++) {
				if ((rate = model.tagRate(j, i)) == Double.MIN_VALUE) {
					continue;
				}
				double value = (pTagScore[j] + tagScore[i]) + rate;
				if (value > maxValue) {
					maxValue = value;
					from[i] = j;
				}
			}
			tagScore[i] = maxValue;
		}
	}

	public static char getTagName(int tag) {
		// TODO Auto-generated method stub
		switch (tag) {
		case 0:
			return 'S';
		case 1:
			return 'B';
		case 2:
			return 'M';
		case 3:
			return 'E';
		default:
			return '?';
		}
	}

}
