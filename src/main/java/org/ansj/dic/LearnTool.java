package org.ansj.dic;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ansj.app.crf.SplitWord;
import org.ansj.domain.Nature;
import org.ansj.domain.NewWord;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.util.Graph;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.CollectionUtil;

/**
 * 新词发现,这是个线程安全的.所以可以多个对象公用一个
 * 
 * @author ansj
 * 
 */
public class LearnTool {

	private SplitWord splitWord = null;

	/**
	 * 是否开启学习机
	 */
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
	public void learn(Graph graph, SplitWord splitWord) {

		this.splitWord = splitWord;

		// 亚洲人名识别
		if (isAsianName) {
			findAsianPerson(graph);
		}

		// 外国人名识别
		if (isForeignName) {
			findForeignPerson(graph);
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

	// 批量将新词加入到词典中
	private void addListToTerm(List<NewWord> newWords) {
		if (newWords.size() == 0)
			return;
		for (NewWord newWord : newWords) {
			addTerm(newWord);
		}
	}

	/**
	 * 增加一个新词到树中
	 * 
	 * @param newWord
	 */
	public void addTerm(NewWord newWord) {
		NewWord temp;
		SmartForest<NewWord> smartForest;
		if ((smartForest = sf.getBranch(newWord.getName())) != null && smartForest.getParam() != null) {
			temp = smartForest.getParam();
			temp.update(newWord.getNature(), newWord.getAllFreq());
		} else {
			count++;
			newWord.setScore(-splitWord.cohesion(newWord.getName()));
			synchronized (sf) {
				sf.addBranch(newWord.getName(), newWord);
			}
		}
	}

	public SmartForest<NewWord> getForest() {
		return this.sf;
	}

	/**
	 * 返回学习到的新词.
	 *
	 * @param num 返回数目.0为全部返回
	 * @return 学习到的新词
	 */
	public List<Entry<String, Double>> getTopTree(int num) {
		return getTopTree(num, null);
	}

	public List<Entry<String, Double>> getTopTree(int num, Nature nature) {
		if (sf.getBranches() == null) {
			return null;
		}
		HashMap<String, Double> hm = new HashMap<>();
		for (int i = 0; i < sf.getBranches().length; i++) {
			valueResult(sf.getBranches()[i], hm, nature);
		}
		List<Entry<String, Double>> sortMapByValue = CollectionUtil.sortMapByValue(hm, -1);
		if (num == 0) {
			return sortMapByValue;
		} else {
			num = Math.min(num, sortMapByValue.size());
			return sortMapByValue.subList(0, num);
		}
	}

	private void valueResult(SmartForest<NewWord> smartForest, HashMap<String, Double> hm, Nature nature) {
		if (smartForest == null || smartForest.getBranches() == null) {
			return;
		}
		for (int i = 0; i < smartForest.getBranches().length; i++) {
			NewWord param = smartForest.getBranches()[i].getParam();
			if (smartForest.getBranches()[i].getStatus() == 3) {
				if (param.isActive() && (nature == null || param.getNature().equals(nature))) {
					hm.put(param.getName(), param.getScore());
				}
			} else if (smartForest.getBranches()[i].getStatus() == 2) {
				if (param.isActive() && (nature == null || param.getNature().equals(nature))) {
					hm.put(param.getName(), param.getScore());
				}
				valueResult(smartForest.getBranches()[i], hm, nature);
			} else {
				valueResult(smartForest.getBranches()[i], hm, nature);
			}
		}
	}

	/**
	 * 尝试激活，新词
	 * 
	 * @param name
	 */
	public void active(String name) {
		SmartForest<NewWord> branch = sf.getBranch(name);
		if (branch != null && branch.getParam() != null) {
			branch.getParam().setActive(true);
		}
	}
}
