package org.ansj.crf;

import org.ansj.splitWord.WordAlert;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 分词
 *
 * @author ansj
 */
public class SplitWord {

    private final Model model;

    private final int[] tagConver;

    private final int[] revTagConver;

    private final int modelEnd1;

    private final int modelEnd2;

    /**
     * 这个对象比较重. 支持多线程, 请尽量重复使用
     *
     * @param model model
     */
    public SplitWord(final Model model) {
        this.tagConver = new int[model.tagNum];
        this.revTagConver = new int[model.tagNum];

        // case 0:'S';case 1:'B';case 2:'M';3:'E';
        model.statusMap.forEach((statKey, statVal) -> {
            switch (statKey) {
                case "S":
                    this.tagConver[statVal] = 0;
                    this.revTagConver[0] = statVal;
                    break;
                case "B":
                    this.tagConver[statVal] = 1;
                    this.revTagConver[1] = statVal;
                    break;
                case "M":
                    this.tagConver[statVal] = 2;
                    this.revTagConver[2] = statVal;
                    break;
                case "E":
                    this.tagConver[statVal] = 3;
                    this.revTagConver[3] = statVal;
                    break;
                default:
                    break;
            }
        });

        this.model = model;
        this.modelEnd1 = model.statusMap.get("S");
        this.modelEnd2 = model.statusMap.get("E");
    }

    public List<String> cut(final char[] chars) {
        return cut(new String(chars));
    }

    public List<String> cut(final String line) {
        if (isBlank(line)) {
            return Collections.emptyList();
        }

        final List<Element> elements = vterbi(line);
        final List<String> result = new LinkedList<>();
        int begin = 0;
        int end = 0;
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            switch (fixTag(element.tag)) {
                case 0:
                    end += element.len;
                    result.add(line.substring(begin, end));
                    begin = end;
                    break;
                case 1:
                    end += element.len;
                    while (fixTag((element = elements.get(++i)).tag) != 3) {
                        end += element.len;
                    }
                    end += element.len;
                    result.add(line.substring(begin, end));
                    begin = end;
                default:
                    break;
            }
        }
        return result;
    }

    private List<Element> vterbi(final String line) {
        final List<Element> elements = WordAlert.str2Elements(line);

        final int length = elements.size();
        if (length == 0) { // 避免空list，下面get(0)操作越界
            return elements;
        } else if (length == 1) {
            elements.set(0, elements.get(0).withTag(revTagConver[0]));
            return elements;
        }

        /**
         * 填充图
         */
        for (int i = 0; i < length; i++) {
            computeTagScore(elements, i);
        }

        // 如果是开始不可能从 m，e开始 ，所以将它设为一个很小的值
        elements.set(0, elements.get(0)
                .withTagScore(revTagConver[2], -1000)
                .withTagScore(revTagConver[3], -1000));
        for (int i = 1; i < length; i++) {
            elements.set(i, elements.get(i).maxFrom(this.model, elements.get(i - 1)));
        }

        // 末位置只能从S,E开始
        Element next = elements.get(elements.size() - 1);
        int maxStatus = next.tagScore(this.modelEnd1) > next.tagScore(this.modelEnd2) ?
                this.modelEnd1 :
                this.modelEnd2;
        next = next.withTag(maxStatus);
        elements.set(elements.size() - 1, next);
        maxStatus = next.from[maxStatus];
        // 逆序寻找
        for (int i = elements.size() - 2; i > 0; i--) {
            elements.set(i, elements.get(i).withTag(maxStatus));
            final Element self = elements.get(i);
            maxStatus = self.from[self.tag];
            next = self;
        }
        elements.set(0, elements.get(0).withTag(maxStatus));
        return elements;
    }

    private void computeTagScore(final List<Element> elements, final int index) {

        final double[] tagScore = new double[model.tagNum];
        for (int i = 0; i < model.ft.length; i++) {
            final char[] chars = new char[model.ft[i].length];
            for (int j = 0; j < chars.length; j++) {
                chars[j] = getElement(elements, index + model.ft[i][j]).name;
            }
            MatrixUtils.dot(tagScore, this.model.feature(i, chars));
        }
        elements.set(index, elements.get(index).withTagScore(tagScore));
    }

    private Element getElement(final List<Element> elements, final int i) {
        if (i < 0) {
            return new Element((char) ('B' + i));
        } else if (i >= elements.size()) {
            return new Element((char) ('B' + i - elements.size() + 1));
        } else {
            return elements.get(i);
        }
    }

    public int fixTag(final int tag) {
        return this.tagConver[tag];
    }

    /**
     * 随便给一个词. 计算这个词的内聚分值, 可以理解为计算这个词的可信度
     *
     * @param word word
     */
    public double cohesion(final String word) {
        if (word.length() == 0) {
            return Integer.MIN_VALUE;
        }

        final List<Element> elements = WordAlert.str2Elements(word);
        for (int i = 0; i < elements.size(); i++) {
            computeTagScore(elements, i);
        }

        final int len = elements.size() - 1;

        double value = elements.get(0).tagScore(revTagConver[1]);
        for (int i = 1; i < len; i++) {
            value += elements.get(i).tagScore(revTagConver[2]);
        }
        value += elements.get(len).tagScore(revTagConver[3]);

        return value < 0 ? 1 : value + 1;
    }
}
