package org.ansj.app.crf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.ansj.app.crf.pojo.Element;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

public abstract class Model {

	public static enum MODEL_TYPE {
		CRF, EMM
	};

	protected Template template = null;

	protected double[][] status = null;

	protected Map<String, Feature> myGrad;

	protected SmartForest<double[][]> smartForest = null;

	public int allFeatureCount = 0;

	private List<Element> leftList = null;

	private List<Element> rightList = null;

	public int end1;

	public int end2;

	/**
	 * 根据模板文件解析特征
	 * 
	 * @param template
	 * @throws IOException
	 */
	private void makeSide(int left, int right) throws IOException {
		// TODO Auto-generated method stub

		leftList = new ArrayList<Element>(Math.abs(left));
		for (int i = left; i < 0; i++) {
			leftList.add(new Element((char) ('B' + i)));
		}

		rightList = new ArrayList<Element>(right);
		for (int i = 1; i < right + 1; i++) {
			rightList.add(new Element((char) ('B' + i)));
		}
	}

	/**
	 * 讲模型写入
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeModel(String path) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		System.out.println("compute ok now to save model!");
		// 写模型
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(path))));

		// 配置模板
		oos.writeObject(template);
		// 特征转移率
		oos.writeObject(status);
		// 总共的特征数
		oos.writeInt(myGrad.size());
		double[] ds = null;
		for (Entry<String, Feature> entry : myGrad.entrySet()) {
			oos.writeUTF(entry.getKey());
			for (int i = 0; i < template.ft.length; i++) {
				ds = entry.getValue().w[i];
				for (int j = 0; j < ds.length; j++) {
					oos.writeByte(j);
					oos.writeFloat((float) ds[j]);
				}
				oos.writeByte(-1);
			}
		}

		oos.flush();
		oos.close();

	}

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
	public static Model loadModel(String modelPath) throws Exception {
		return loadModel(new FileInputStream(modelPath));

	}

	public static Model loadModel(InputStream modelStream) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(modelStream)));

			Model model = new Model() {

				@Override
				public void writeModel(String path) throws FileNotFoundException, IOException {
					// TODO Auto-generated method stub
					throw new RuntimeException("you can not to calculate ,this model only use by cut ");
				}

			};

			model.template = (Template) ois.readObject();

			model.makeSide(model.template.left, model.template.right);

			int tagNum = model.template.tagNum;

			int featureNum = model.template.ft.length;

			model.smartForest = new SmartForest<double[][]>(0.8);

			model.status = (double[][]) ois.readObject();

			// 总共的特征数
			double[][] w = null;
			String key = null;
			int b = 0;
			int featureCount = ois.readInt();
			for (int i = 0; i < featureCount; i++) {
				key = ois.readUTF();
				w = new double[featureNum][0];
				for (int j = 0; j < featureNum; j++) {
					while ((b = ois.readByte()) != -1) {
						if (w[j].length == 0) {
							w[j] = new double[tagNum];
						}
						w[j][b] = ois.readFloat();
					}
				}
				model.smartForest.add(key, w);
			}

			return model;
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}

	public double[] getFeature(int featureIndex, char... chars) {
		// TODO Auto-generated method stub
		SmartForest<double[][]> sf = smartForest;
		sf = sf.getBranch(chars);
		if (sf == null || sf.getParam() == null) {
			return null;
		}
		return sf.getParam()[featureIndex];
	}

	/**
	 * tag转移率
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public double tagRate(int s1, int s2) {
		// TODO Auto-generated method stub
		return status[s1][s2];
	}

}