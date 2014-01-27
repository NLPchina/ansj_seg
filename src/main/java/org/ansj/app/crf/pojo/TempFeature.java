package org.ansj.app.crf.pojo;

public class TempFeature {
	public int id;
	public int featureId;
	public String name;

	public TempFeature(String str, int tagNum) {
		String[] split = str.split(" ");
		this.id = Integer.parseInt(split[0]) / tagNum;
		int splitIndex = split[1].indexOf(":");
		this.featureId = Integer.parseInt(split[1].substring(1, splitIndex));
		this.name = split[1].substring(splitIndex + 1).replace("/", "");
	}

}
