package org.ansj.app.crf;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.ansj.app.crf.model.CRFModel;
import org.ansj.app.crf.model.CRFppTxtModel;
import org.ansj.app.crf.model.WapitiCRFModel;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.MapCount;

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
	 * 判断当前数据流是否是本实例
	 * 
	 * @param is
	 * @return
	 */
	public abstract boolean checkModel(byte[] bytes) throws IOException;

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
	public static Model load(String name, String modelPath) throws Exception {
		InputStream is = null;
		try {
			is = IOUtil.getInputStream(modelPath);

			byte[] bytes = new byte[100];

			is.read(bytes);

			Model model = new CRFModel(name);

			if (model.checkModel(bytes)) {
				model.loadModel(modelPath);
				return model;
			}

			model = new CRFppTxtModel(name);

			if (model.checkModel(bytes)) {
				model.loadModel(modelPath);
				return model;
			}

			model = new WapitiCRFModel(name);

			if (model.checkModel(bytes)) {
				model.loadModel(modelPath);
				return model;
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}

		throw new Exception("I did not know waht type of model by file " + modelPath);

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

	/**
	 * 增加特征到特征数中
	 * 
	 * @param cs
	 * @param tempW
	 */
	protected static void printFeatureTree(String cs, float[] tempW) {
		String name = "*";
		if (tempW.length == 4) {
			name = "U";
		}

		name += "*" + ((int) cs.charAt(cs.length() - 1) - Config.FEATURE_BEGIN + 1) + ":" + cs.substring(0, cs.length() - 1);
		for (int i = 0; i < tempW.length; i++) {
			if (tempW[i] != 0) {
				System.out.println(name + "\t" + Config.getTagName(i / 4 - 1) + "\t" + Config.getTagName(i % 4) + "\t" + tempW[i]);
			}

		}
	}

	/**
	 * 将model序列化到硬盘
	 * 
	 * @param path
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeModel(String path) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = null;
		try {

			oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(new File(path))));

			oos.writeUTF(CRFModel.version);

			oos.writeObject(status);

			oos.writeObject(config.getTemplate());

			Map<String, float[]> map = featureTree.toMap();

			MapCount<Integer> mc = new MapCount<Integer>();

			for (float[] v : map.values()) {
				mc.add(v.length);
			}

			for (Entry<Integer, Double> entry : mc.get().entrySet()) {
				int win = entry.getKey();
				oos.writeInt(win);// 宽度
				oos.writeInt(entry.getValue().intValue());// 个数
				for (Entry<String, float[]> e : map.entrySet()) {
					if (e.getValue().length == win) {
						oos.writeUTF(e.getKey());
						float[] value = e.getValue();
						for (int i = 0; i < win; i++) {
							oos.writeFloat(value[i]);
						}
					}
				}
			}
			oos.writeInt(0);
			oos.writeInt(0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				oos.flush();
				oos.close();
			}
		}
	}
}