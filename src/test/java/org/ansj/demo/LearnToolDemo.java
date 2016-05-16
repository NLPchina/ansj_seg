package org.ansj.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.domain.NewWord;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.util.IOUtil;

/**
 * 新词发现工具
 * 
 * @author ansj
 * 
 */
public class LearnToolDemo {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

		// 构建一个新词学习的工具类。这个对象。保存了所有分词中出现的新词。出现次数越多。相对权重越大。
		LearnTool learnTool = new LearnTool();

		// 进行词语分词。也就是nlp方式分词，这里可以分多篇文章
		NlpAnalysis.parse("说过，社交软件也是打着沟通的平台，让无数寂寞男女有了肉体与精神的寄托。", learnTool);
		NlpAnalysis.parse("其实可以打着这个需求点去运作的互联网公司不应只是社交类软件与可穿戴设备，还有携程网，去哪儿网等等，订房订酒店多好的寓意", learnTool);
		NlpAnalysis.parse("张艺谋的卡宴，马明哲的戏", learnTool);

		// 取得学习到的topn新词,返回前10个。这里如果设置为0则返回全部
		System.out.println(learnTool.getTopTree(10));

		// 只取得词性为Nature.NR的新词
		System.out.println(learnTool.getTopTree(10, Nature.NR));

		/**
		 * 将训练结果序列写入到硬盘中
		 */
		List<Entry<String, Double>> topTree = learnTool.getTopTree(0);
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Double> entry : topTree) {
			sb.append(entry.getKey() + "\t" + entry.getValue()+"\n");
		}
		IOUtil.Writer("learnTool.snap", IOUtil.UTF8, sb.toString());
		sb = null;

		/**
		 * reload训练结果
		 */
		learnTool = new LearnTool() ;
		HashMap<String, Double> loadMap = IOUtil.loadMap("learnTool.snap", IOUtil.UTF8, String.class, Double.class);
		for (Entry<String, Double> entry : loadMap.entrySet()) {
			learnTool.addTerm(new NewWord(entry.getKey(), Nature.NW, entry.getValue())) ;
			learnTool.active(entry.getKey()) ;
		}
		System.out.println(learnTool.getTopTree(10));
		
		new File("learnTool.snap").delete() ;
	}
}
