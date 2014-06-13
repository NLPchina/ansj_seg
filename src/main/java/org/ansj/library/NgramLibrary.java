package org.ansj.library;

import org.ansj.domain.BigramEntry;
import org.ansj.domain.Term;
import org.ansj.util.MyStaticValue;

/**
 * 两个词之间的关联
 * 
 * @author ansj
 * 
 */
public class NgramLibrary {
	private static BigramEntry[][] bigramTables = null;
	static {
		try {
			long start = System.currentTimeMillis();
			bigramTables = MyStaticValue.getBigramTables();
			MyStaticValue.LIBRARYLOG.info("init ngram ok use time :" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 查找两个词与词之间的频率
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getTwoWordFreq(Term from, Term to) {
		if (from.termNatures().id < 0) {
			return 0;
		}
		BigramEntry[] be = bigramTables[from.termNatures().id];

		int index = binarySearch(be, to.termNatures().id);

		if (index < 0) {
			return 0;
		}
		return be[index].freq;
	}

	/**
	 * 二分查找在数组中是否有.继续抄袭jdk.sun不要告偶
	 * 
	 * @param be
	 * @param key
	 * @return
	 */
	private static int binarySearch(BigramEntry[] be, int key) {
		int low = 0;
		int high = be.length - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			BigramEntry midVal = be[mid];
			int cmp = midVal.id - key;

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	public static void setBigramTables(BigramEntry[][] bigramTables) {
		NgramLibrary.bigramTables = bigramTables;
	}

}
