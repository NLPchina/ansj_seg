package org.ansj.app.crf.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import org.ansj.app.crf.Config;
import org.ansj.app.crf.Model;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;

/**
 * 加载ansj格式的crfmodel,目前此model格式是通过crf++ 或者wapiti生成的
 * 
 * @author Ansj
 *
 */
public class CRFModel extends Model {
	
	public static final String version = "ansj1" ;

	public CRFModel(String name) {
		super(name);
	}

	@Override
	public void loadModel(String modelPath) throws Exception {
		loadModel(IOUtil.getInputStream(modelPath));
	}
	
	public void loadModel(InputStream is) throws Exception{
		
		ObjectInputStream ois = null;

		long start = System.currentTimeMillis();
		try {

			ois = new ObjectInputStream(new GZIPInputStream(is));

			ois.readUTF();

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
			if(is!=null){
				is.close();
			}
			if (ois != null) {
				ois.close();
			}
		}
	}

	@Override
	public boolean checkModel(String modelPath) throws IOException {
		ObjectInputStream inputStream = null ;
		try {
			
			inputStream = new ObjectInputStream(new GZIPInputStream(new FileInputStream(modelPath)));

			String version = inputStream.readUTF() ;

			if (version.equals("ansj1")) { // 加载ansj,model
				return true;
			}
			
		}catch(ZipException ze){
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(inputStream!=null){
				inputStream.close(); 
			}
		}
		return false ;
	}

}
