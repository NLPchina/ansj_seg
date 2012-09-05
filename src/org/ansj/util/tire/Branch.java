package org.ansj.util.tire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 树中的一个子页
 * 
 * @author ansj
 */
public class Branch implements Comparable<Branch> {

	private int c;
	List<Branch> all = new ArrayList<Branch>();
	public double value;

	public Branch(int c, double value) {
		this.c = c;
		this.value = value;
	}

	public Branch add(Branch branch) {
		// TODO Auto-generated method stub
		int index = Collections.binarySearch(all, branch);
		if (index < 0) {
			all.add(branch);
			Collections.sort(all);
		} else {
			Branch temp = all.get(index);

			if (branch.value >= 0) {
				temp.value = branch.value;
			}
			branch = temp;
		}
		return branch;
	}

	public Branch get(int c) {
		Branch branch = new Branch(c, -1);
		int index = Collections.binarySearch(all, branch);
		if (index < 0) {
			return null;
		} else {
			return all.get(index);
		}
	}

	@Override
	public int compareTo(Branch o) {
		// TODO Auto-generated method stub
		if (this.c == o.c) {
			return 0;
		} else if (this.c > o.c) {
			return 1;
		} else {
			return -1;
		}
	}

}
