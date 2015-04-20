package org.ansj.app.crf.model;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.TempFeature;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

public class CRFModel extends Model {

	/**
	 * 解析crf++生成的可可视txt文件
	 */
	private void parseFile(String path) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = IOUtil.getReader(path, IOUtil.UTF8);

		String temp = null;

		reader.readLine();// version
		reader.readLine();// cost-factor

		int maxId = Integer.parseInt(reader.readLine().split(":")[1].trim());// read
		// maxId

		reader.readLine();// xsize
		reader.readLine(); // line

		Map<String, Integer> statusMap = new HashMap<String, Integer>();

		// read status
		int tagNum = 0;
		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp)) {
				break;
			}
			statusMap.put(temp, tagNum);
			tagNum++;
		}

		status = new double[tagNum][tagNum];

		// read config
		StringBuilder sb = new StringBuilder();
		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp)) {
				break;
			}
			sb.append(temp + "\n");
		}

		this.template = Template.parse(sb.toString());

		this.template.tagNum = tagNum;

		this.template.statusMap = statusMap;

		int featureNum = template.ft.length;

		TempFeature[] tempFeatureArr = new TempFeature[maxId / tagNum];

		String[] split = reader.readLine().split(" ");// read first B

		int bIndex = Integer.parseInt(split[0]) / tagNum;

		TempFeature tf = null;

		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp)) {
				break;
			}
			tf = new TempFeature(temp, tagNum);
			tempFeatureArr[tf.id] = tf;
		}

		myGrad = new HashMap<String, Feature>();

		Feature feature = null;
		// 填充
		for (int i = 0; i < tempFeatureArr.length; i++) {

			// 读取转移模板
			if (i == bIndex) {
				for (int j = 0; j < tagNum; j++) {
					for (int j2 = 0; j2 < tagNum; j2++) {
						status[j][j2] = Double.parseDouble(reader.readLine());
					}
				}
				i += tagNum - 1;
				continue;
			}
			tf = tempFeatureArr[i];
			feature = myGrad.get(tf.name);
			if (feature == null) {
				feature = new Feature(featureNum, tagNum);
				myGrad.put(tf.name, feature);
			}
			for (int j = 0; j < tagNum; j++) {
				double f = Double.parseDouble(reader.readLine());
				feature.update(tf.featureId, j, f);
			}
		}
	}

	/**
	 * 解析crf 生成的模型 txt文件
	 * 
	 * @param modelPath
	 * @param templatePath
	 * @return
	 * @throws Exception
	 */
	public static Model parseFileToModel(String modelPath) throws Exception {
		CRFModel model = new CRFModel();
		model.parseFile(modelPath);
		return model;
	}
}
