package org.ansj.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.splitWord.analysis.UserDefineAnalysis;
import org.nlpcn.commons.lang.util.IOUtil;

public class IndexAnalysisTest {
	public static void main(String[] args) throws IOException {
//		System.out.println(IndexAnalysis.parse("这个问题经常的会被人提及。我一般会这样说，学习一种能让你开发大型系统的语言"));
//		System.out.println(ToAnalysis.parse("对了我现在有个demo想在vps上面展示...但是暂用内存太大..大家有什么好的建议么"));
//		System.out.println(IndexAnalysis.parse("工信处女干事"));
//		System.out.println(IndexAnalysis.parse("Asdf as 12312 ")) ;
//		System.out.println(IndexAnalysis.parse("Asdf as 12312 ")) ;
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

		
//		System.out.println(IndexAnalysis.parse("季德胜蛇药片10片*6板 清热"));
//		
//		System.out.println(IndexAnalysis.parse("季德胜蛇药片"));
//
		UserDefineLibrary.insertWord("蛇药片", "n", 1000);
		
//		System.out.println(IndexAnalysis.parse("季德胜蛇药片10片*6板 清热"));
//
//		System.out.println(IndexAnalysis.parse("季德胜蛇药片"));
//		
//		System.out.println(IndexAnalysis.parse("鲁花一级花生油"));
//		
//		System.out.println(IndexAnalysis.parse("上海虹桥机场旅游和服务都是一流的"));
//		
//		System.out.println(IndexAnalysis.parse("北京地铁"));
		
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
