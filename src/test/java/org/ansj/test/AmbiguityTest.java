package org.ansj.test;

import org.ansj.splitWord.analysis.ToAnalysis;

public class AmbiguityTest {
	public static void main(String[] args) {
		// 习近平 习近平 nr
		// 李民工作 李民 nr 工作 vn
		// 三个和尚 三个 m 和尚 n
		// 的确定不 的确 d 定 v 不 v
		// 大和尚 大 a 和尚 n
		// 张三和 张三 nr 和 c
		// 动漫游戏 动漫 n 游戏 n
		// 邓颖超生前 邓颖超 nr 生前 t
		System.out.println(ToAnalysis.parse("学习近平和李克强将称为一种时尚!"));
//		System.out.println(ToAnalysis.parse("李民工作了一天!"));
//		System.out.println(ToAnalysis.parse("三个和尚抬水喝!"));
		System.out.println(ToAnalysis.parse("我想说,这事的确定不下来,我得想!"));
//		System.out.println(ToAnalysis.parse("小和尚剃了一个和大和尚一样的和尚头"));
//		System.out.println(ToAnalysis.parse("我喜欢玩动漫游戏"));
//		System.out.println(ToAnalysis.parse("邓颖超生前最喜欢的一个"));
	}
}
