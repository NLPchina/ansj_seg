package org.ansj.test;

import java.io.IOException;

import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.util.MyStaticValue;

public class IndexAnalysisTest {
	public static void main(String[] args) throws IOException {
		MyStaticValue.isNumRecognition=true;
		MyStaticValue.isQuantifierRecognition = false;
		MyStaticValue.userLibrary=null ;
		UserDefineLibrary.insertWord("蛇药片", "n", 1000);
		UserDefineLibrary.insertWord("蛇药", "n", 1000);
		UserDefineLibrary.insertWord("鲁花", "n", 1000);
		UserDefineLibrary.insertWord("隐形", "n", 1000);
		UserDefineLibrary.insertWord("眼镜", "n", 1000);
		UserDefineLibrary.insertWord("隐形眼镜", "n", 1000);
		UserDefineLibrary.insertWord("海昌 ", "n", 1000);
		UserDefineLibrary.insertWord("美瞳", "n", 1000);

		System.out.println(IndexAnalysis.parse("海昌 润洁除蛋白隐形眼镜美瞳护理液 500+120ml"));

		System.out.println(IndexAnalysis.parse("隐形眼镜 护理"));

		System.out.println(IndexAnalysis.parse("海昌 润洁除蛋白隐形眼镜美瞳护理液 500+120ml"));

		System.out.println(IndexAnalysis.parse("季德胜蛇药片"));

		System.out.println(IndexAnalysis.parse("鲁花一级花生油"));

		System.out.println(IndexAnalysis.parse("上海虹桥机场旅游和服务都是一流的"));

		System.out.println(IndexAnalysis.parse("北京地铁"));
		

		UserDefineLibrary.insertWord("隐形", "attr", 1000);
		UserDefineLibrary.insertWord("眼镜", "attr", 1000);
		UserDefineLibrary.insertWord("隐形", "attr", 1000);
		UserDefineLibrary.insertWord("形眼", "attr", 1000);
		UserDefineLibrary.insertWord("隐形眼", "attr", 1000);
		System.out.println(IndexAnalysis.parse("隐形眼镜"));
		
//		long start = System.currentTimeMillis() ;
//				
//		IndexAnalysis indexAnalysis = new IndexAnalysis(new InputStreamReader(new FileInputStream("/home/ansj/temp/one_day_all.txt"))) ;
//		
//		while(indexAnalysis.next()!=null){
//			
//		}
//		System.out.println(System.currentTimeMillis()-start);
	}
}
