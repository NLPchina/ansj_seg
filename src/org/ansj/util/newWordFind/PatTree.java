package org.ansj.util.newWordFind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Term;

/**
 * 用来查找重复串的pat树,和业务绑定吧.不打算做通用的了.需求也不是很多,这是线程安全的
 * 
 * @author ansj
 */
public class PatTree {
	private static final String ROOT_NAME = "ROOT";
	public Node root = new Node(ROOT_NAME);
	// 新词发现的概率.限定了新词条数的输出
	// 结果集
	private TreeSet<Node> result = new TreeSet<Node>();

	/**
	 * 增加term链到树中
	 * 
	 * @param terms
	 */
	public void addList(List<Term> terms) {
		int length = terms.size();
		// 长度太短直接忽略
		if (length < 2) {
			return;
		}
		List<Term> all = null;
		for (int i = 0; i < length; i++) {
			all = new ArrayList<Term>(length - i);
			for (int j = i; j < length; j++) {
				all.add(terms.get(j));
			}
			root.addList(all);
		}
	}

	/**
	 * 树中的节点:ps一定切记要不对外开放
	 * 
	 * @author ansj
	 * 
	 */
	class Node implements Comparable<Node> {
		// 此节点的所有偏移量
		private int count;
		// 此节点的term值
		private String name;
		// 子页节点
		private Map<String, Node> nodes = new HashMap<String, Node>();

		public Node(String name) {
			this.name = name;
		}

		/**
		 * 增加一个term链到树中
		 * 
		 * @param nodes
		 */
		public void addList(List<Term> list) {
			// 进行term添加
			Node node = null;
			Map<String, Node> tempNodes = this.nodes;
			Term term = null;
			for (int i = 0; i < list.size(); i++) {
				term = list.get(i);
				node = tempNodes.get(term.getName());
				if (node != null) {
					node.count++;
				} else {
					node = new Node(term.getName());
					tempNodes.put(term.getName(), node);
				}
				tempNodes = node.nodes;
			}
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return this.name.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			return this.name.equals(((Node) obj).name);
		}

		@Override
		public int compareTo(Node o) {
			// TODO Auto-generated method stub
			// 先比数字
			if (this.count < o.count) {
				return 1;
			} else if (this.count > o.count) {
				return -1;
			}
			// 再比长度
			if (this.name.length() < o.name.length()) {
				return 1;
			} else {
				return -1;
			}
		}

		public int getCount() {
			return count;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.name + ":" + this.count;
		}

	}

	public List<Node> getWords() {
		Collection<Node> values = this.root.nodes.values();
		for (Node node : values) {
			getWords(node, node.name);
		}

		List<Node> all = new ArrayList<Node>();
		boolean flag = false;
		// 迭代替代重复子节点
		for (Iterator<Node> iterator = result.iterator(); iterator.hasNext();) {
			Node value = iterator.next();
			flag = false;
			for (Node node : all) {
				if (node.name.contains(value.name)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				all.add(value);
			}
		}
		return all;
	}

	/**
	 * 深度遍历树寻找最大公共串
	 * 
	 * @return
	 */
	private void getWords(Node node, String sb) {
		// TODO Auto-generated method stub
		Set<Entry<String, Node>> entrySet = node.nodes.entrySet();
		for (Entry<String, Node> entry : entrySet) {
			Node temp = entry.getValue();
			if (temp.count < 1) {
				continue;
			}
			String word = sb + temp.name;
			// 放入到hash中
			temp.name = word;
			result.add(temp);
			// 递归深度
			getWords(temp, word);
		}
	}
}
