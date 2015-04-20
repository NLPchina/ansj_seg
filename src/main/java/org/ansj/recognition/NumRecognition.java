package org.ansj.recognition;

import org.ansj.domain.Term;
import org.ansj.util.MyStaticValue;
import org.ansj.util.TermUtil;

public class NumRecognition {

	/**
	 * 数字+数字合并,zheng
	 * 
	 * @param terms
	 */
	public static void recognition(Term[] terms) {
		int length = terms.length - 1;
		Term from = null;
		Term to = null;
		Term temp = null;
		for (int i = 0; i < length; i++) {
			if (terms[i] == null) {
				continue;
			} else if (".".equals(terms[i].getName()) || "．".equals(terms[i].getName())) {
				// 如果是.前后都为数字进行特殊处理
				to = terms[i].to();
				from = terms[i].from();
				if (from.termNatures().numAttr.flag && to.termNatures().numAttr.flag) {
					from.setName(from.getName() + "." + to.getName());
					TermUtil.termLink(from, to.to());
					terms[to.getOffe()] = null;
					terms[i] = null;
					i = from.getOffe() - 1;
				}
				continue;
			} else if (!terms[i].termNatures().numAttr.flag) {
				continue;
			}

			temp = terms[i];
			// 将所有的数字合并
			while ((temp = temp.to()).termNatures().numAttr.flag) {
				terms[i].setName(terms[i].getName() + temp.getName());
			}
			// 如果是数字结尾
			if (MyStaticValue.isQuantifierRecognition && temp.termNatures().numAttr.numEndFreq > 0) {
				terms[i].setName(terms[i].getName() + temp.getName());
				temp = temp.to();
			}

			// 如果不等,说明terms[i]发生了改变
			if (terms[i].to() != temp) {
				TermUtil.termLink(terms[i], temp);
				// 将中间无用元素设置为null
				for (int j = i + 1; j < temp.getOffe(); j++) {
					terms[j] = null;
				}
				i = temp.getOffe() - 1;
			}
		}

	}

}
