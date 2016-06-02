package org.ansj.app.crf.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.ansj.app.crf.Config;
import org.ansj.app.crf.Model;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.MapCount;

/**
 * 加载ansj格式的crfmodel,目前此model格式是通过crf++ 或者wapiti生成的
 * 
 * @author Ansj
 *
 */
public class CRFModel extends Model {

	public CRFModel(String name) {
		super(name);
	}

	@Override
	public void loadModel(String modelPath) throws Exception {
		ObjectInputStream ois = null;

		long start = System.currentTimeMillis();
		try {

			ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(new File(modelPath))));

			ois.read(new byte[20]);// 读取头20个byte

			this.status = (float[][]) ois.readObject();

			int[][] template = (int[][]) ois.readObject();

			this.config = new Config(template);

			int win = 0;
			int size = 0;
			String name = null;

			featureTree = new SmartForest<float[]>();
			float[] value = null;
			do {
				win = ois.readInt();
				size = ois.readInt();

				for (int i = 0; i < size; i++) {
					name = ois.readUTF();
					value = new float[win];
					for (int j = 0; j < value.length; j++) {
						value[j] = ois.readFloat();
					}
					featureTree.add(name, value);
				}

			} while (win == 0 || size == 0);

			LOG.info("load crf model ok ! use time :" + (System.currentTimeMillis() - start));

		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}

	@Override
	public boolean checkModel(byte[] bytes) throws IOException {

		try {
			
			InputStream inputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));

			byte[] b = new byte[bytes.length];

			inputStream.read(b);

			String string = new String(b);

			if (string.startsWith("ansj1")) { // 加载ansj,model
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
