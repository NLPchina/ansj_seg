package org.ansj.recognition.arrimpl;

import org.ansj.domain.PersonNatureAttr;
import org.ansj.domain.Term;
import org.ansj.library.DATDictionary;
import org.ansj.recognition.TermArrRecognition;
import org.ansj.util.Graph;
import org.nlpcn.commons.lang.viterbi.Node;
import org.nlpcn.commons.lang.viterbi.Viterbi;
import org.nlpcn.commons.lang.viterbi.function.Score;
import org.nlpcn.commons.lang.viterbi.function.Values;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 人名识别工具类
 *
 * @author ansj
 */
public class PersonRecognition implements TermArrRecognition {

	//BE BCD  XD BZ
	private static final float[] FACTORY = {0.16271366224044456f, 0.8060521860870434f, 0.031234151672511947f};

	private static final Set<Integer> filter = new HashSet<>();

	static {
		filter.add('B' * 1000 + 'E');
		filter.add('B' * 1000 + 'C');

		filter.add('C' * 1000 + 'D');

		filter.add('D' * 1000 + 'L');
		filter.add('D' * 1000 + 'M');
		filter.add('D' * 1000 + 'A');

		filter.add('E' * 1000 + 'L');
		filter.add('E' * 1000 + 'M');
		filter.add('E' * 1000 + 'A');

		filter.add('K' * 1000 + 'B');
		filter.add('K' * 1000 + 'X');

		filter.add('L' * 1000 + 'B');
		filter.add('L' * 1000 + 'X');
		filter.add('L' * 1000 + 'A');

		filter.add('M' * 1000 + 'B');
		filter.add('M' * 1000 + 'X');

		filter.add('X' * 1000 + 'D');

		filter.add('Y' * 1000 + 'L');
		filter.add('Y' * 1000 + 'M');
		filter.add('Y' * 1000 + 'A');

		filter.add('Z' * 1000 + 'L');
		filter.add('Z' * 1000 + 'M');
		filter.add('Z' * 1000 + 'A');

		filter.add('A' * 1000 + 'B');
		filter.add('A' * 1000 + 'X');
		filter.add('A' * 1000 + 'A');

		//0B 1C 2D 3E 4K 5L 6M 7X 8Y 9Z 10A

	}

	private PersonNode[][] nodes = null;

	private int beginOff;

	@Override
	public void recognition(Graph graph) {

		Term[] terms = graph.terms;

		Term from = null, first = null, sencond = null, third = null, last = null;

		PersonNatureAttr fromPna = null, fPna = null, sPna = null, tPna = null, lPna = null;


		//0B 1C 2D 3E 4K 5L 6M 7X 8Y 9Z 10A

		nodes = new PersonNode[terms.length + 1][11];

		beginOff = terms[0].getOffe();


		for (int i = 0; i < terms.length; i++) {
			first = terms[i];

			if (first == null) {
				continue;
			}
			fPna = getPersonNature(first);
			setNode(first, 'A');

			fPna = getPersonNature(first);
			if (fPna == null || !fPna.isActive()) {
				continue;
			}

			sencond = first.to();
			if (sencond.getOffe() == terms.length || sencond.getName().length() > 2) { //说明到结尾了,或者后面长度不符合规则
				continue;
			}

			third = sencond.to();
			from = first.from();

			//XD
			if (first.getName().length() == 2) {
				setNode(from, 'K');
				setNode(from, 'M');
				setNode(first, 'X');
				setNode(sencond, 'D');
				setNode(third, 'M');
				setNode(third, 'L');
				continue;
			}

			setNode(from, 'K');
			setNode(from, 'M');
			setNode(first, 'B');
			setNode(third, 'M');
			setNode(third, 'L');
			//BZ
			if (sencond.getName().length() == 2) {
				setNode(sencond, 'Z');
				continue;
			} else {//BE
				setNode(sencond, 'E');
			}


			if (third.getOffe() == terms.length || third.getName().length() > 1) { //说明到结尾了,或者后面长度不符合规则
				continue;
			}

			//BCD
			setNode(first, 'B');
			setNode(sencond, 'C');
			setNode(third, 'D');
			setNode(third.to(), 'M');
			setNode(third.to(), 'L');

		}

		nodes[0][6] = null;
		nodes[0][10] = new PersonNode(10, "B", DATDictionary.person("BEGIN").getA());
		nodes[0][10] = new PersonNode(4, "B", DATDictionary.person("BEGIN").getK());
		nodes[terms.length - 1][6] = null;


		List<PersonNode> result = new Viterbi<PersonNode>(nodes, new Values<PersonNode>() {
			@Override
			public int step(Node<PersonNode> node) {
				if(node.getObj()==null)return 0;
				if(node.getObj().name==null)return  0;
				return node.getObj().name.length();
			}

			@Override
			public double selfSscore(Node<PersonNode> node) {
				if(node.getObj()==null)return 0;
				if(node.getObj().name==null)return  0;
				return node.getObj().score;
			}
		}).compute(new Score<PersonNode>() {
			@Override
			public double score(Node<PersonNode> from, Node<PersonNode> to) {
				if (from == null || to == null) {
					return -10000;
				}
        if (from.getT() == null || to.getT() == null) {
          return -10000;
        }

				if (!filter.contains(from.getT().tag * 1000 + to.getT().tag)) {
					return -10000;
				}
				return from.getScore() + to.getSelfScore();
			}

			@Override
			public boolean sort() {
				return true;
			}
		});

		System.out.println(result);

	}

	private void setNode(Term term, char tag) {
		int index = term.getOffe() - beginOff + 1;
		PersonNatureAttr pna = getPersonNature(term);
		double score = 0D;

		//0B 1C 2D 3E 4K 5L 6M 7X 8Y 9Z 10A
		switch (tag) {
			case 'B':
				score = pna.getB();
				tag = 0;
				break;
			case 'C':
				score = pna.getC();
				tag = 1;
				break;
			case 'D':
				score = pna.getD();
				tag = 2;
				break;
			case 'E':
				score = pna.getE();
				tag = 3;
				break;
			case 'K':
				score = pna.getK();
				tag = 4;
				break;
			case 'L':
				score = pna.getL();
				tag = 5;
				break;
			case 'M':
				score = pna.getM();
				tag = 6;
				break;
			case 'X':
				score = pna.getX();
				tag = 7;
				break;
			case 'Y':
				score = pna.getY();
				tag = 8;
				break;
			case 'Z':
				score = pna.getZ();
				tag = 9;
				break;
			case 'A':
				score = pna.getA();
				tag = 10;
				break;
		}

		if (nodes[index][tag] == null) {
			nodes[index][tag] = new PersonNode(tag, term.getName(), -Math.log(score));
		}
	}


	/**
	 * 获得一个term的personnature
	 *
	 * @param term
	 * @return
	 */
	private PersonNatureAttr getPersonNature(Term term) {

		if (term.termNatures().personAttr != PersonNatureAttr.NULL) {
			return term.termNatures().personAttr;
		}

		PersonNatureAttr person = DATDictionary.person(term.getName()); //解决词典外单字问题

		if (person != null) {
			return person;
		}

		person = DATDictionary.person(":" + term.getNatureStr());

		if (person == null) {
			return PersonNatureAttr.NULL;
		}

		return person;
	}

	class PersonNode {
		public int tag;
		public String name;
		public double score;

		public PersonNode(int tag, String name, double score) {
			this.tag = tag;
			this.name = name;
			this.score = score;
		}

		public String toString() {
			return name + "," + tag + "," + score;
		}
	}

}
