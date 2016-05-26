package org.ansj.app.crf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.nlpcn.commons.lang.tire.domain.SmartForest;

public abstract class Model {

	protected static final Logger LOG = Logger.getLogger("CRF");

	protected String name;

	protected Config config;

	protected SmartForest<float[]> featureTree = null;

	protected float[][] status = new float[Config.TAG_NUM][Config.TAG_NUM];

	public int allFeatureCount = 0;

	public Model(String name) {
		this.name = name;
	};

	/**
	 * 模型读取
	 * 
	 * @param path
	 * @return
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Model load(String modelPath) throws Exception {
		return null;

	}

	/**
	 * 不同的模型实现自己的加载模型类
	 * 
	 * @throws Exception
	 */
	public abstract void loadModel(String modelPath) throws Exception;

	/**
	 * 获得特征所在权重数组
	 * 
	 * @param featureStr
	 * @return
	 */
	public float[] getFeature(char... chars) {
		if (chars == null) {
			return null;
		}
		SmartForest<float[]> sf = featureTree;
		sf = sf.getBranch(chars);
		if (sf == null || sf.getParam() == null) {
			return null;
		}
		return sf.getParam();
	}

	public String getName() {
		return this.name;
	};

	public Config getConfig() {
		return this.config;
	}

	/**
	 * tag转移率
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public float tagRate(int s1, int s2) {
		return status[s1][s2];
	}

}