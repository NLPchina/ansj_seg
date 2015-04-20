package org.ansj.app.crf.model;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

public class WapitiCRFModel extends Model {

	Map<String, Integer> statusMap = null;

	private int tagNum = 0;

	private int maxSize = 0;

	private void parseFile(String path, String templatePath) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = IOUtil.getReader(path, IOUtil.UTF8);

		statusMap = new HashMap<String, Integer>();

		// read config
		String content = IOUtil.getContent(IOUtil.getReader(templatePath, IOUtil.UTF8));

		this.template = Template.parse(content);

		myGrad = new HashMap<String, Feature>();

		String temp = null;

		List<String> statusLines = new ArrayList<String>();

		// 填充
		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isNotBlank(temp) && temp.charAt(0) == 'b') {
				statusLines.add(temp);
			}
		}

		for (String str : statusLines) {
			String[] split = str.split("\t");
			addStatus(split[1]);
			addStatus(split[2]);
		}
		this.template.tagNum = tagNum;
		status = new double[tagNum][tagNum];
		for (String str : statusLines) {
			String[] split = str.split("\t");
			status[statusMap.get(split[1])][statusMap.get(split[2])] = Double.parseDouble(split[3]);
		}
		
		//fix status range sbme
		status[statusMap.get("S")][statusMap.get("E")] = Double.MIN_VALUE ;
		status[statusMap.get("S")][statusMap.get("M")] = Double.MIN_VALUE ;
		
		status[statusMap.get("B")][statusMap.get("B")] = Double.MIN_VALUE ;
		status[statusMap.get("B")][statusMap.get("S")] = Double.MIN_VALUE ;
		
		status[statusMap.get("M")][statusMap.get("S")] = Double.MIN_VALUE ;
		status[statusMap.get("M")][statusMap.get("B")] = Double.MIN_VALUE ;
		
		status[statusMap.get("E")][statusMap.get("M")] = Double.MIN_VALUE ;
		status[statusMap.get("E")][statusMap.get("E")] = Double.MIN_VALUE ;
		
		
		
		IOUtil.close(reader);

		// read feature
		reader = IOUtil.getReader(path, IOUtil.UTF8);
		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isNotBlank(temp) && temp.charAt(0) == 'u') {
				parseGrad(temp, template.ft.length);
			}
			// 后面的不保留
			if (maxSize > 0 && myGrad.size() > maxSize) {
				break;
			}
		}
		IOUtil.close(reader);

		this.template.statusMap = statusMap;
	}

	private void parseGrad(String temp, int featureNum) {

		String[] split = temp.split("\t");

		int mIndex = split[0].indexOf(":");

		String name = fixName(split[0].substring(mIndex + 1));

		int fIndex = Integer.parseInt(split[0].substring(1, mIndex));
		int sta = statusMap.get(split[2]);
		double step = Double.parseDouble(split[3]);

		Feature feature = myGrad.get(name);
		if (feature == null) {
			feature = new Feature(featureNum, tagNum);
			myGrad.put(name, feature);
		}
		feature.update(fIndex, sta, step);
	}

	private String fixName(String substring) {
		// TODO Auto-generated method stub
		String[] split = substring.split(" ");
		StringBuffer sb = new StringBuffer();
		for (String string : split) {
			if (string.startsWith("_x")) {
				string = String.valueOf((char) ('B' + Integer.parseInt(string.substring(2))));
			}
			sb.append(string);
		}

		return sb.toString();
	}

	public void addStatus(String sta) {
		if (statusMap.containsKey(sta)) {
			return;
		}
		statusMap.put(sta, tagNum);
		tagNum++;
	}

	/**
	 * 解析wapiti 生成的模型 dump 出的文件
	 * 
	 * @param modelPath
	 * @param templatePath
	 * @param maxSize
	 *            模型存储内容大小
	 * @throws Exception
	 */
	public static Model parseFileToModel(String modelPath, String templatePath, int maxSzie) throws Exception {
		WapitiCRFModel model = new WapitiCRFModel();
		model.maxSize = maxSzie;
		model.parseFile(modelPath, templatePath);
		return model;
	}

}
