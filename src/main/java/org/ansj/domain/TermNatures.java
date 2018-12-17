package org.ansj.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 每一个term都拥有一个词性集合
 *
 * @author ansj
 */
public class TermNatures implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final TermNatures M = new TermNatures(TermNature.M);

	public static final TermNatures M_ALB = new TermNatures(TermNature.M);

	public static final TermNatures NR = new TermNatures(TermNature.NR);

	public static final TermNatures EN = new TermNatures(TermNature.EN);

	public static final TermNatures END = new TermNatures(TermNature.END, 50610, -1);

	public static final TermNatures BEGIN = new TermNatures(TermNature.BEGIN, 50610, 0);

	public static final TermNatures NT = new TermNatures(TermNature.NT);

	public static final TermNatures NS = new TermNatures(TermNature.NS);

	public static final TermNatures NRF = new TermNatures(TermNature.NRF);

	public static final TermNatures NW = new TermNatures(TermNature.NW);

	public static final TermNatures NULL = new TermNatures(TermNature.NULL);
	;

	/**
	 * 关于这个term的所有词性
	 */
	public TermNature[] termNatures = null;

	/**
	 * 数字属性
	 */
	public NumNatureAttr numAttr = NumNatureAttr.NULL;

	/**
	 * 人名词性
	 */
	public PersonNatureAttr personAttr = PersonNatureAttr.NULL;

	/**
	 * 默认词性
	 */
	public Nature nature = null;

	/**
	 * 所有的词频
	 */
	public int allFreq = 0;

	/**
	 * 词的id
	 */
	public int id = -2;

	/**
	 * 构造方法.一个词对应这种玩意
	 *
	 * @param natureStr
	 * @param id        双数组中的索引
	 */
	public TermNatures(String natureStr, int id) {
		this.id = id;

		//{n=2707,q=0,q_mq=0,r=0,v=1}

		natureStr = natureStr.substring(1, natureStr.length() - 1);
		String[] split = natureStr.split(",");

		List<TermNature> all = new ArrayList<>();

		TermNature maxTermNature = null;
		int maxFreq = 0, frequency = -1;
		int size = 0;

		for (int i = 0; i < split.length; i++) {
			String[] strs = split[i].split("=");
			frequency = Integer.parseInt(strs[1]);
			allFreq+= frequency ;
			if (strs[0].startsWith("q_")) { //内置词性
				this.numAttr = new NumNatureAttr(false, true, strs[0].replaceFirst("q_", ""));
			} else {
				TermNature termNature = new TermNature(strs[0], frequency);
				all.add(termNature);
				if (maxFreq <= frequency) {
					maxFreq = frequency;
					maxTermNature = termNature;
				}
				size++;
			}
		}

		termNatures = all.toArray(new TermNature[all.size()]);

		if (maxTermNature != null) {
			this.nature = maxTermNature.nature;
		} else {
			if (numAttr != NumNatureAttr.NULL) {
				this.nature = numAttr.nature;
			}
		}
	}

	public TermNatures(TermNature termNature) {
		termNatures = new TermNature[1];
		this.termNatures[0] = termNature;
		this.nature = termNature.nature;
	}

	public TermNatures(TermNature termNature, int allFreq, int id) {
		this.id = id;
		termNatures = new TermNature[1];
		termNature.frequency = allFreq;
		this.termNatures[0] = termNature;
		nature = termNature.nature ;
		this.allFreq = allFreq;
	}


	public void setPersonNatureAttr(PersonNatureAttr personAttr) {
		this.personAttr = personAttr;
	}

}
