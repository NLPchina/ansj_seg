package org.ansj.splitWord;

import lombok.Getter;
import org.ansj.crf.SplitWord;
import org.ansj.Nature;
import org.ansj.NewWord;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 新词发现,这是个线程安全的.所以可以多个对象公用一个
 *
 * @author ansj
 */
public class LearnTool {

    /**
     * 是否开启学习机
     */
    private final boolean isAsianName;

    private final boolean isForeignName;

    /**
     * 告诉大家你学习了多少个词了
     */
    @Getter
    private int count;

    /**
     * 新词发现的结果集.可以序列化到硬盘.然后可以当做训练集来做.
     */
    private final SmartForest<NewWord> sf;

    public LearnTool(final boolean isAsianName, final boolean isForeignName) {
        this.isAsianName = isAsianName;
        this.isForeignName = isForeignName;
        this.sf = new SmartForest<>();
    }

    public LearnTool() {
        this(true, true);
    }

    /**
     * 公司名称学习.
     *
     * @param graph graph
     */
    public void learn(final Graph graph, final SplitWord splitWord) {
        if (isAsianName) {// 亚洲人名识别
            new AsianPersonRecognition(graph.terms).getNewWords().forEach(newWord -> addTerm(newWord, splitWord));
        }
        if (isForeignName) {// 外国人名识别
            new ForeignPersonRecognition(graph.terms).getNewWords().forEach(newWord -> addTerm(newWord, splitWord));
        }
    }

    /**
     * 增加一个新词到树中
     *
     * @param newWord   newWord
     * @param splitWord splitWord
     */
    public void addTerm(final NewWord newWord, final SplitWord splitWord) {
        SmartForest<NewWord> smartForest;
        NewWord temp;
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

    private static void valueResult(
            final SmartForest<NewWord> smartForest,
            final HashMap<String, Double> hm,
            final Nature nature
    ) {
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
     * @param name name
     */
    public void active(final String name) {
        final SmartForest<NewWord> branch = sf.getBranch(name);
        if (branch != null && branch.getParam() != null) {
            branch.getParam().setActive(true);
        }
    }
}
