package org.ansj.util.tire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * tire树内部是二分查找
 * @author ansj
 *
 */
public class TireTree {
	
	private Branch root = new Branch(0, -1);
	
	public TireTree(int[][] ints ,double dFactor[]){
		for (int i = 0; i < ints.length; i++) {
			add(ints[i], dFactor[i]);
		}
	}

	public static void main(String[] args) {
		int[][] ints = { { 1, 1, 2, 3 }, { 1, 1, 3 }, { 1, 1, 4 }, { 1, 1, 25 }, { 1, 2, 3 }, { 1, 4, 4 }, { 1, 4 }, { 1, 6 }, { 1, 23, 3 }, { 1, 25 },
				{ 2, 3, 2, 3 }, { 2, 3 }, { 4, 4 }, { 5, 1 }, { 23, 3 } };
		double dFactor[] = { 0.003606, 0.000021, 0.001314, 0.000315, 0.656624, 0.000021, 0.146116, 0.009136, 0.000042, 0.038971, 0, 0.090367, 0.000273,
				0.009157, 0.034324, 0 };
		TireTree tree = new TireTree(ints,dFactor) ;
		for (int i = 0; i < ints.length; i++) {
			tree.add(ints[i], dFactor[i]);
		}
		
		int[] intTemp = { 2 };
		tree.search(intTemp);

	}

	/**
	 * 增加一个字符串到tire树中
	 * 
	 * @param str
	 */
	public void add(int[] ints, double factory) {
		Branch temp = root;
		for (int i = 0; i < ints.length; i++) {
			if (i == ints.length - 1) {
				temp = temp.add(new Branch(ints[i], factory));
			} else {
				temp = temp.add(new Branch(ints[i], -1));
			}
		}
	}

	public void search(int[] ints) {
		Branch temp = root;
		for (int i = 0; i < ints.length; i++) {
			temp = temp.get(ints[i]);
			if (temp == null) {
				System.out.println("木有找到呢!!!!");
				return;
			}
		}
		print(temp);

	}
	
	public Branch getRoot(){
		return root ;
	}

	private static void print(Branch branch) {
		List<Branch> all = branch.all;
		for (Branch branch2 : all) {
			if (branch2.value > -1) {
				System.out.println(branch2.value);
			}
			print(branch2);
		}
	}
}

