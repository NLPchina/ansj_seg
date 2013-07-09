package org.ansj.app.newWord;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import love.cq.domain.SmartForest;
import love.cq.util.CollectionUtil;

import org.ansj.app.newWord.PatHashMap.Node;
import org.ansj.domain.NewWord;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.CompanyRecogntion;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.util.Graph;

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
	public boolean isCompany = true;

	public boolean isNewWord = true;

	public boolean isAsianName = true;

	public boolean isForeignName = true;

	/**
	 * 告诉大家你学习了多少个词了
	 */
	public int count;

	/**
	 * 新词发现的结果集.可以序列化到硬盘.然后可以当做训练集来做.
	 */
	private final SmartForest<NewWord> sf = new SmartForest<NewWord>();

	/**
	 * 公司名称学习.
	 * 
	 * @param graph
	 */
	public void learn(Graph graph) {
		// 机构名识别
		if (isCompany) {
			findCompany(graph);
		}

		// 亚洲人名识别
		if (isAsianName) {
			findAsianPerson(graph);
		}

		// 外国人名识别
		if (isForeignName) {
			findForeignPerson(graph);
		}

		// 新词发现
		if (isNewWord) {
			newWordDetection(graph);
		}

	}

	private void findAsianPerson(Graph graph) {
		List<NewWord> newWords = new AsianPersonRecognition(graph.terms).getNewWords();
		addListToTerm(newWords);
	}

	private void findForeignPerson(Graph graph) {
		List<NewWord> newWords = new ForeignPersonRecognition(graph.terms).getNewWords();
		addListToTerm(newWords);
	}

	/**
	 * 公司名称查找
	 * 
	 * @param graph
	 */
	private void findCompany(Graph graph) {
		List<NewWord> newWords = new CompanyRecogntion(graph.terms).getNewWords();
		addListToTerm(newWords);
	}

	// 批量将新词加入到词典中
	private void addListToTerm(List<NewWord> newWords) {
		if (newWords.size() == 0)
			return;
		for (NewWord newWord : newWords) {
			addTerm(newWord);
		}
	}

	/**
	 * 新词发现
	 * 
	 * @param graph
	 */
	private void newWordDetection(Graph graph) {
		Collection<Node> newWords = null;
		try {
			newWords = new NewWordDetection().getNewWords(graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (newWords == null)
			return;
		NewWord newWord = null;
		for (Node node : newWords) {
			newWord = new NewWord(node.getName(), TermNatures.NW, node.getScore(), node.getFreq());
			addTerm(newWord);
		}
	}

	/**
	 * 增加一个新词到树中
	 * 
	 * @param newWord
	 */
	public void addTerm(NewWord newWord) {
		NewWord temp = null;
		SmartForest<NewWord> smartForest = null;
		if ((smartForest = sf.getBranch(newWord.getName())) != null && smartForest.getParam() != null) {
			temp = smartForest.getParam();
			temp.update(newWord.getScore(), newWord.getNature(), newWord.getAllFreq());
		} else {
			count++;
			// 设置名字为空,节省内存空间
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
		return getTopTree(num, null);
	}

	public List<Entry<String, Double>> getTopTree(int num, TermNatures nature) {
		if (sf.branches == null) {
			return null;
		}
		HashMap<String, Double> hm = new HashMap<String, Double>();
		for (int i = 0; i < sf.branches.length; i++) {
			valueResult(sf.branches[i], hm, nature);
		}
		List<Entry<String, Double>> sortMapByValue = CollectionUtil.sortMapByValue(hm, -1);
		if (num == 0) {
			return sortMapByValue;
		} else {
			num = Math.min(num, sortMapByValue.size());
			return sortMapByValue.subList(0, num);
		}
	}

	private void valueResult(SmartForest<NewWord> smartForest, HashMap<String, Double> hm, TermNatures nature) {
		// TODO Auto-generated method stub
		for (int i = 0; i < smartForest.branches.length; i++) {
			NewWord param = smartForest.branches[i].getParam();
			if (smartForest.branches[i].getStatus() == 3) {
				if (nature == null || param.getNature().equals(nature)) {
					hm.put(param.getName(), param.getScore());
				}
			} else if (smartForest.branches[i].getStatus() == 2) {
				if (nature == null || param.getNature().equals(nature)) {
					hm.put(param.getName(), param.getScore());
				}
				valueResult(smartForest.branches[i], hm, nature);
			} else {
				valueResult(smartForest.branches[i], hm, nature);
			}
		}
	}
}
