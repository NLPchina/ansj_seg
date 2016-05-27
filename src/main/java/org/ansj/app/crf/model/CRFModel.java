package org.ansj.app.crf.model;

import org.ansj.app.crf.Model;

/**
 * 加载CRF+生成的crf模型,测试使用的CRF++版本为:CRF++-0.58
 * 
 * 下载地址:https://taku910.github.io/crfpp/#download 在这里感谢作者所做的工作.
 * 
 * @author Ansj
 *
 */
public class CRFModel extends Model {

	public CRFModel(String name) {
		super(name);
	}

	/**
	 * 解析crf++生成的可可视txt文件
	 */
	public void loadModel(String modelPath) throws Exception {
		
	}

}
