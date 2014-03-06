package org.ansj.demo;

import love.cq.domain.SmartForest;
import love.cq.splitWord.SmartGetWord;

public class SummaryDemo {

	public static void main(String[] args) {

		// String content =
		// "【傅盛谈特斯拉：用互联网思维做汽车】@傅盛 ：特斯拉不仅仅是一辆电动车，更是一辆伟大的汽车。为什么特斯拉会成功？我觉得主要有三点：一、它确定了自己的核心定位；二、在于极简化的设计；三、就是直销模式。它的成功源于用不一样的互联网思维做汽车。 http://t.cn/8Fk5XWA";
		//
		// String title = null;
		//
		// SummaryComputer summaryComputer = new SummaryComputer(300, title,
		// content);
		//
		// Summary summary = summaryComputer.toSummary();
		//
		// System.out.println(summary.getKeyWords()); // 关键词
		//
		// System.out.println(summary.getSummary()); // 摘要
		//
		// TagContent tw = new TagContent("<begin>", "<end>");
		//
		// String tagContent = tw.tagContent(summary); // 标记后的摘要
		//
		// System.out.println(tagContent);

		SmartForest<Integer> sf = new SmartForest<Integer>();

		sf.add("互联网", 1);
		sf.add("汽车", 1);
		sf.add("傅盛", 1);
		sf.add("傅盛谈", 1);
		sf.add("特斯拉", 1);

		SmartGetWord<Integer> sg = new SmartGetWord<Integer>(sf, "傅盛谈特斯拉：用互联网思维做汽车");

		String temp = null;
		while ((temp = sg.getFrontWords()) != null) {
			System.out.println(temp + "\t" + sg.offe);
		}

	}
}
