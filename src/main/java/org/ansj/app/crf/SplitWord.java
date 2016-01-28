package org.ansj.app.crf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ansj.app.crf.pojo.Element;
import org.ansj.app.crf.pojo.Template;
import org.ansj.util.MatrixUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.WordAlert;

/**
 * 分词
 * 
 * @author ansj
 * 
 */
public class SplitWord {

	private Model model = null;

	private int[] tagConver = null;

	private int[] revTagConver = null;
	
	//空的分词组件
	public SplitWord(){
		
	}

	/**
	 * 这个对象比较重。支持多线程，请尽量重复使用
	 * 
	 * @param model
	 * @throws Exception
	 */
	public SplitWord(Model model) {
		this.model = model;
		tagConver = new int[model.template.tagNum];
		revTagConver = new int[model.template.tagNum];
		Set<Entry<String, Integer>> entrySet = model.template.statusMap.entrySet();

		// case 0:'S';case 1:'B';case 2:'M';3:'E';
		for (Entry<String, Integer> entry : entrySet) {
			if ("S".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 0;
				revTagConver[0] = entry.getValue();
			} else if ("B".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 1;
				revTagConver[1] = entry.getValue();
			} else if ("M".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 2;
				revTagConver[2] = entry.getValue();
			} else if ("E".equals(entry.getKey())) {
				tagConver[entry.getValue()] = 3;
				revTagConver[3] = entry.getValue();
			}
		}

		model.end1 = model.template.statusMap.get("S");
		model.end2 = model.template.statusMap.get("E");

	};

	public List<String> cut(char[] chars) {
		return cut(new String(chars));
	}

	public List<String> cut(String line) {

		if (StringUtil.isBlank(line)) {
			return Collections.emptyList();
		}

		List<Element> elements = vterbi(line);

		List<String> result = new ArrayList<String>();

		Element e = null;
		int begin = 0;
		int end = 0;

		for (int i = 0; i < elements.size(); i++) {
			e = elements.get(i);
			switch (fixTag(e.getTag())) {
			case 0:
				end += e.len;
				result.add(line.substring(begin, end));
				begin = end;
				break;
			case 1:
				end += e.len;
				while (fixTag((e = elements.get(++i)).getTag()) != 3) {
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
		List<Element> elements = str2Elements(line);

		int length = elements.size();
		if (length == 0) { // 避免空list，下面get(0)操作越界
			return elements;
		}
		if (length == 1) {
			elements.get(0).updateTag(revTagConver[0]);
			return elements;
		}

		/**
		 * 填充图
		 */
		for (int i = 0; i < length; i++) {
			computeTagScore(elements, i);
		}

		// 如果是开始不可能从 m，e开始 ，所以将它设为一个很小的值
		elements.get(0).tagScore[revTagConver[2]] = -1000;
		elements.get(0).tagScore[revTagConver[3]] = -1000;
		for (int i = 1; i < length; i++) {
			elements.get(i).maxFrom(model, elements.get(i - 1));
		}

		// 末位置只能从S,E开始
		Element next = elements.get(elements.size() - 1);
		Element self = null;
		int maxStatus = next.tagScore[model.end1] > next.tagScore[model.end2] ? model.end1 : model.end2;
		next.updateTag(maxStatus);
		maxStatus = next.from[maxStatus];
		// 逆序寻找
		for (int i = elements.size() - 2; i > 0; i--) {
			self = elements.get(i);
			self.updateTag(maxStatus);
			maxStatus = self.from[self.getTag()];
			next = self;
		}
		elements.get(0).updateTag(maxStatus);
		return elements;

	}

	private void computeTagScore(List<Element> elements, int index) {
		double[] tagScore = new double[model.template.tagNum];

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
			return new Element((char) ('B' + i));
		} else if (i >= elements.size()) {
			return new Element((char) ('B' + i - elements.size() + 1));
		} else {
			return elements.get(i);
		}
	}

	public int fixTag(int tag) {
		return tagConver[tag];
	}

	/**
	 * 随便给一个词。计算这个词的内聚分值，可以理解为计算这个词的可信度
	 * 
	 * @param word
	 */
	public double cohesion(String word) {

		if (word.length() == 0) {
			return Integer.MIN_VALUE;
		}

		List<Element> elements = str2Elements(word);

		for (int i = 0; i < elements.size(); i++) {
			computeTagScore(elements, i);
		}

		double value = elements.get(0).tagScore[revTagConver[1]];

		int len = elements.size() - 1;

		for (int i = 1; i < len; i++) {
			value += elements.get(i).tagScore[revTagConver[2]];
		}

		value += elements.get(len).tagScore[revTagConver[3]];

		if (value < 0) {
			return 1;
		} else {
			value += 1;
		}

		return value;
	}

	public static List<Element> str2Elements(String str) {

		if (str == null || str.trim().length() == 0) {
			return Collections.emptyList();
		}

		char[] chars = WordAlert.alertStr(str);
		int maxLen = chars.length - 1;
		List<Element> list = new ArrayList<Element>();
		Element element = null;
		out: for (int i = 0; i < chars.length; i++) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				element = new Element('M');
				list.add(element);
				if (i == maxLen) {
					break out;
				}
				char c = chars[++i];
				while (c == '.' || c == '%' || (c >= '0' && c <= '9')) {
					if (i == maxLen) {
						break out;
					}
					c = chars[++i];
					element.len();
				}
				i--;
			} else if (chars[i] >= 'a' && chars[i] <= 'z') {
				element = new Element('W');
				list.add(element);
				if (i == maxLen) {
					break out;
				}
				char c = chars[++i];
				while (c >= 'a' && c <= 'z') {
					if (i == maxLen) {
						break out;
					}
					c = chars[++i];
					element.len();
				}
				i--;
			} else {
				list.add(new Element(chars[i]));
			}
		}
		return list;
	}

}
