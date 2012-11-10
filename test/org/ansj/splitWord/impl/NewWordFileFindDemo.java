package org.ansj.splitWord.impl;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.newWordFind.NewWordFind;
import org.ansj.util.recognition.NatureRecognition;

/**
 * 这是一个新词发现的简单例子.
 * @author ansj
 *
 */
public class NewWordFileFindDemo {
	public static void main(String[] args) throws IOException {
		String content = "据共同社消息，东京警视厅称，位于东京都港区的中国驻日本大使馆27日收到装有疑似来福枪子弹的信封。　  据警方透露，信封中并无威胁信息，寄信人一栏写着“野田佳彦”。警视厅认为这或是针对钓鱼岛问题的恶作剧，目前正在就子弹的真假进行调查。日本网友对此议论纷纷：  网友评论：  ★是哪个野田佳彦寄了这封信？？  ★是不是又是小泽干的？  ★信下面真的有名字吗？我记得没写名字啊！哈哈  ★ 野田是野猫战斗机吗？这都能做到？  ★ 在日本能送来复枪子弹的人可不是一般人啊，加油野田！  ★ 这是在干什么？激怒日本吗？其实是为了贬低日本吧！  ★ 我们的野田啊！！！  ★ 还真难说啊，不过确实应该重新看待最近的野田了，关于那个消费增值税，我想说的是FUCK!  ★ 不接受亲笔信就接受来复枪的子弹信吧！  ★ 真的是子弹吗？真是厉害啊！！  ★ 不愧是野田啊，真牛！！哈哈  ★哪个邮局送的啊？彻底查出来！  ★ 送子弹算什么本事应该扔手榴弹发火箭炮进去！！  ★ 事实上野田是一个好战分子啊！！  ★无聊！！";
		System.out.println(NewWordFind.getNewWords(content)); ;
		System.out.println(NewWordFind.entryName);
		
		String title = "中国使馆收到“野田”装有子弹的来信 日网友乐开花" ;
		List<Term> paser = ToAnalysis.paser(title) ;
		
		new NatureRecognition(paser).recogntion() ;
		
		for (Term term : paser) {
			String natureStr = term.getNatrue().natureStr ;
			if (natureStr.contains("n")){
				System.out.print(term.getName()+" , ");
			}
			
		}
	}
}	