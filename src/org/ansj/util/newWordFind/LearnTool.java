package org.ansj.util.newWordFind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import love.cq.domain.SmartForest;

import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.util.CollectionUtil;
import org.ansj.util.Graph;
import org.ansj.util.newWordFind.PatTree.Node;
import org.ansj.util.recognition.CompanyRecogntion;

/**
 * 新词发现,这是个线程安全的.所以可以多个对象公用一个
 * 
 * @author ansj
 * 
 */
public class LearnTool {

	/**
	 * 是否开启学习机
	 */
	public boolean isLearn = true;

	public boolean isCompany = false;

	public boolean isNewWord = true;

	/**
	 * 告诉大家你学习了多少个词了
	 */
	public int count;

	/**
	 * 新词发现的结果集.可以序列化到硬盘.然后可以当做训练集来做.
	 */
	private final SmartForest<NewWord> sf = new SmartForest<NewWord>();

	/**
	 * 学习.分词前先把要分的词放入进行学习
	 * 
	 * @param content
	 * @throws IOException
	 */
	private void learn(List<Term> list) throws IOException {
		List<Node> newWords = new NewWordFind().getNewWords(list);
		if (newWords == null)
			return;
		NewWord newWord = null;
		for (Node node : newWords) {
			newWord = new NewWord(node.getName(), -0.002 * (node.getCount() + 1), node.getCount() + 1, TermNatures.NW);
			addTerm(newWord);
		}
	}

	/**
	 * 公司名称学习.
	 * 
	 * @param graph
	 */
	public void learn(Graph graph) {
		if (isCompany) {
			findCompany(graph);
		}
		if (isNewWord) {
			findNewWord(graph);
		}

	}

	/**
	 * 公司名称查找
	 * 
	 * @param graph
	 */
	private void findCompany(Graph graph) {
		List<NewWord> newWords = new CompanyRecogntion(graph.terms).getNewWords();
		if (newWords.size() > 0)
			System.out.println(newWords);
		for (NewWord newWord : newWords) {
			addTerm(newWord);
		}
	}

	/**
	 * 新词发现
	 * 
	 * @param graph
	 */
	private void findNewWord(Graph graph) {
		// 进入新词发现,学习
		List<Term> result = new ArrayList<Term>();
		int length = graph.terms.length - 1;
		for (int i = 0; i < length; i++) {
			if (graph.terms[i] != null) {
				result.add(graph.terms[i]);
			}
		}

		try {
			learn(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 增加一个新词到树中
	 * 
	 * @param newWord
	 */
	public void addTerm(NewWord newWord) {
		SmartForest<NewWord> smartForest = null;
		if ((smartForest = sf.getBranch(newWord.getName())) != null && smartForest.getParam() != null) {
			newWord = smartForest.getParam();
			newWord.update(newWord.getScore(), 1, newWord.getNature());
		} else {
			count++;
			synchronized (sf) {
				sf.add(newWord.getName(), newWord);
			}
		}
	}

	public SmartForest<NewWord> getForest() {
		return this.sf;
	}

	/**
	 * 返回学习到的新词.
	 * 
	 * @param num
	 *            返回数目.0为全部返回
	 * @return
	 */
	public List<Entry<String, Double>> getTopTree(int num) {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		for (int i = 0; i < sf.branches.length; i++) {
			valueResult(sf.branches[i], hm);
		}
		List<Entry<String, Double>> sortMapByValue = CollectionUtil.sortMapByValue(hm,-1);
		if (num == 0) {
			return sortMapByValue;
		} else {
			num = Math.min(num, sortMapByValue.size()) ;
			return sortMapByValue.subList(0, num);
		}
	}

	private void valueResult(SmartForest<NewWord> smartForest, HashMap<String, Double> hm) {
		// TODO Auto-generated method stub
		for (int i = 0; i < smartForest.branches.length; i++) {
			NewWord param = smartForest.branches[i].getParam();
			if (smartForest.branches[i].getStatus() == 3) {
				hm.put(param.getName(), param.getScore());
			} else if (smartForest.branches[i].getStatus() == 2) {
				hm.put(param.getName(), param.getScore());
				valueResult(smartForest.branches[i], hm);
			} else {
				valueResult(smartForest.branches[i], hm);
			}
		}
	}
}
