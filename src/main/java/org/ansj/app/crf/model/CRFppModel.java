//package org.ansj.app.crf.model;
//
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//
//import org.ansj.app.crf.Model;
//
///**
// * 加载CRF+生成的crf二进制模型,测试使用的CRF++版本为:CRF++-0.58
// * 
// * 下载地址:https://taku910.github.io/crfpp/#download 在这里感谢作者所做的工作.
// * 
// * @author Ansj
// *
// */
//public class CRFppModel extends Model {
//
//	public CRFppModel(String name) {
//		super(name);
//	}
//
//	/**
//	 * 解析crf++生成的可可视文件
//	 */
//	public void loadModel(String modelPath) throws Exception {
//
//		FileInputStream fileInputStream = new FileInputStream(modelPath);
//
//		DataInputStream dis = new DataInputStream(fileInputStream);
//
//		System.out.println();
//
//	}
//
//	public static void main(String[] args) throws Exception {
//		new CRFppModel("test").loadModel("/Users/sunjian/Documents/src/CRF++-0.58/test/model");
//
//		// System.out.println("---------------------------");
//		//
//		// int u = 1;
//		//
//		// byte[] b = new byte[4];
//		//
//		// b[0] = (byte) (u);
//		// b[1] = (byte) (u >> 8);
//		// b[2] = (byte) (u >> 16);
//		// b[3] = (byte) (u >> 24);
//		//
//		// System.out.println(Arrays.toString(b));
//		//
//		// System.out.println("---------------------------");
//		//
//		// b = new byte[4];
//		//
//		// b[0] = -72;
//		// b[1] = 36;
//		// b[2] = 86;
//		// b[3] = 0;
//		//
//		// System.out.println((int) (b[0] | b[1] << 8 | b[2] << 16 | b[3] <<
//		// 24));
//		//
//		// System.out.println((char) 66);
//		// System.out.println((char) 69);
//		// System.out.println((char) 77);
//		// System.out.println((char) 83);
//		// System.out.println((char) 104);
//		//
//		// System.out.println((char) 85);
//		// System.out.println((char) 48);
//		// System.out.println((char) 49);
//		// System.out.println((char) 58);
//		// System.out.println((char) 37);
//		// System.out.println((char) 120);
//	}
//
//}
