package org.ansj.app.crf;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import love.cq.util.StringUtil;

import org.ansj.app.crf.model.Model;
import org.ansj.app.crf.model.Template;
import org.ansj.app.crf.pojo.Element;
import org.ansj.util.MatrixUtil;
import org.ansj.util.WordAlert;

/**
 * 分词
 * 
 * @author ansj
 * 
 */
public class SplitWord {

    private Model model = null;

    public SplitWord(Model model) {
        this.model = model;
    };

    public List<String> cut(String line) {
        // TODO Auto-generated method stub

        if (StringUtil.isBlank(line)) {
            return Collections.emptyList();
        }

        List<Element> elements = vterbi(line);
        LinkedList<String> result = new LinkedList<String>();

        Element e = null;
        int begin = 0;
        int end = 0;
        for (int i = 0; i < elements.size(); i++) {
            e = elements.get(i);
            switch (e.getTag()) {
                case 0:
                    end += e.len;
                    result.add(line.substring(begin, end));
                    begin = end;
                    break;
                case 1:
                    end += e.len;
                    while ((e = elements.get(++i)).getTag() != 3) {
                        end += e.len;
                    }
                    end += e.len;
                    result.add(line.substring(begin, end));
                    begin = end;
                default:
                    break;
            }
        }
        return result;
    }

    private List<Element> vterbi(String line) {
        List<Element> elements = WordAlert.str2Elements(line);
        int length = elements.size();

        if (length == 1) {
            elements.get(0).updateTag(0);
            return elements;
        }

        /**
         * 填充图
         */
        for (int i = 0; i < length; i++) {
            computeTagScore(elements, i);
        }

        for (int i = 1; i < length; i++) {
            elements.get(i).maxFrom(model, elements.get(i - 1));
        }

        // 首位置只能从0,3开始
        Element next = elements.get(elements.size() - 1);
        Element self = null;
        int maxStatus = next.tagScore[0] > next.tagScore[3] ? 0 : 3;
        next.updateTag(maxStatus);
        maxStatus = next.from[maxStatus];
        // System.out.println(next + "\t" +
        // Element.getTagName(next.from[next.getTag()]));
        // 逆序寻找
        for (int i = elements.size() - 2; i > 0; i--) {
            self = elements.get(i);
            self.updateTag(maxStatus);
            maxStatus = self.from[self.getTag()];
            // System.out.println(self + "\t" +
            // Element.getTagName(self.from[self.getTag()]));
            next = self;
        }
        elements.get(0).updateTag(maxStatus);

        // for (Element element : elements) {
        // System.out.println(Arrays.toString(element.from));
        // }

        return elements;

    }

    private void computeTagScore(List<Element> elements, int index) {
        // TODO Auto-generated method stub

        double[] tagScore = new double[Model.TAG_NUM];

        Template t = model.template;
        char[] chars = null;
        for (int i = 0; i < t.ft.length; i++) {
            chars = new char[t.ft[i].length];
            for (int j = 0; j < chars.length; j++) {
                chars[j] = getElement(elements, index + t.ft[i][j]).name;
            }
            MatrixUtil.dot(tagScore, model.getFeature(i, chars));
        }
        elements.get(index).tagScore = tagScore;
    }

    private Element getElement(List<Element> elements, int i) {
        // TODO Auto-generated method stub
        if (i < 0) {
            return new Element('B');
        } else if (i >= elements.size()) {
            return new Element('E');
        } else {
            return elements.get(i);
        }
    }
}
