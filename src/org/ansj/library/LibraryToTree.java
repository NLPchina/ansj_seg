package org.ansj.library;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;



public class LibraryToTree {

	// 是否有下一个
	private static boolean hasNext = true;
	// 是否是一个词
	private static boolean isWords = true;
	
	protected Branch head  ;

	Iterator<String> it = null;

	public void addLibrary(Set<String> keyWords) {
		it = keyWords.iterator();
		head = new Branch('h', 0, null,null );
		Branch branch = head;
		String line = null ;
		String[] temps = null ;
		while (it.hasNext()) {
			line = it.next() ;
			temps = line.split("	") ;
			char[] chars = temps[1].toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (chars.length == (i + 1)) {
					isWords = true;
					hasNext = false;
				} else {
					isWords = false;
					hasNext = true;
				}
				int status = 1;
				if (isWords && hasNext) {
					status = 2;
				}

				if (!isWords && hasNext) {
					status = 1;
				}

				if (isWords && !hasNext) {
					status = 3;
				}
				HashMap<String,Integer> nature = null ;
				if(status>1){
					nature = new HashMap<String,Integer>() ;
					nature.put(temps[0], Integer.parseInt(temps[2])) ;
				}
				branch.add(new Branch(chars[i], status,nature,new String(chars,0,i+1)));
				branch = (Branch) branch.get(chars[i]);
			}
			branch = head;
		}
	}
}


class Branch {
	/**
	 * status 此字的状态1，继续 2，是个词语但是还可以继续 ,3确定 nature 词语性质 0.未知 . 1是姓 . 2 是职位名称 3
	 * 是数量级的词 . 4 是数字词语 5 是标点
	 */
	Branch[] branches = new Branch[0];
	private char c;
	// 状态
	private byte status = 1;
	// 索引
	private short index = -1;
	// 词性,和相对于此词性的权重
	private HashMap<String,Integer> natures = new HashMap<String,Integer>();
	//真实的单词
	private String value ;
	// 单独查找出来的对象
	Branch branch = null;

	public Branch add(Branch branch) {
		if ((this.branch = this.get(branch.getC())) != null) {
			switch (branch.getStatus()) {
			case 1:
				if (this.branch.getStatus() == 2) {
					this.branch.setStatus(2);
				}
				if (this.branch.getStatus() == 3) {
					this.branch.setStatus(2);
				}
				break;
			case 2:
				this.branch.setStatus(2);
			case 3:
				if (this.branch.getStatus() == 2) {
					this.branch.setStatus(2);
				}
				if (this.branch.getStatus() == 1) {
					this.branch.setStatus(2);
				}
			}
			if(branch.getNatures()!=null){
				if(this.branch.getNatures()==null){
					this.branch.setNatures(branch.getNatures()) ;
				}else{
					this.branch.getNatures().putAll(branch.getNatures());
				}
			}
			return this.branch;
		}
		index++;
		if ((index + 1) > branches.length) {
			branches = Arrays.copyOf(branches, index + 1);
		}
		branches[index] = branch;
		AnsjArrays.sort(branches);
		return branch;
	}

	public Branch(char c, int status, HashMap<String,Integer> nature ,String value) {
		this.c = c;
		this.status = (byte) status;
		this.natures = nature;
		this.value = value ;
	}

	int i = 0;

	public Branch get(char c) {
		int i = AnsjArrays.binarySearch(branches, c);
		if (i > -1) {
			return branches[i];
		}
		return null;
	}

	public boolean contains(char c) {
		if (AnsjArrays.binarySearch(branches, c) > -1) {
			return true;
		} else {
			return false;
		}
	}

	public int compareTo(char c) {
		if (this.c > c) {
			return 1;
		} else if (this.c < c) {
			return -1;
		} else
			return 0;
	}

	public boolean equals(char c) {
		if (this.c == c) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString(){
		return this.value +"	"+status+"	"+natures ;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return c;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = (byte) status;
	}

	public char getC() {
		return this.c;
	}

	public HashMap<String,Integer> getNatures() {
		return natures;
	}

	public void setNatures(HashMap<String,Integer> nature) {
		this.natures = nature;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	
	
}

class AnsjArrays {
	private static final int INSERTIONSORT_THRESHOLD = 7;

	/**
	 * 二分法查找.摘抄了jdk的东西..只不过把他的自动装箱功能给去掉了
	 * 
	 * @param branches
	 * @param c
	 * @return
	 */
	public static int binarySearch(Branch[] branches, char c) {
		int high = branches.length - 1;
		if (branches.length < 1) {
			return high;
		}
		int low = 0;
		while (low <= high) {
			int mid = (low + high) >>> 1;
			int cmp = branches[mid].compareTo(c);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -1; // key not found.
	}

	public static void sort(Branch[] a) {
		Branch[] aux = (Branch[]) a.clone();
		mergeSort(aux, a, 0, a.length, 0);
	}

	public static void sort(Branch[] a, int fromIndex, int toIndex) {
		rangeCheck(a.length, fromIndex, toIndex);
		Branch[] aux = copyOfRange(a, fromIndex, toIndex);
		mergeSort(aux, a, fromIndex, toIndex, -fromIndex);
	}

	private static void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
		if (fromIndex > toIndex)
			throw new IllegalArgumentException("fromIndex(" + fromIndex
					+ ") > toIndex(" + toIndex + ")");
		if (fromIndex < 0)
			throw new ArrayIndexOutOfBoundsException(fromIndex);
		if (toIndex > arrayLen)
			throw new ArrayIndexOutOfBoundsException(toIndex);
	}

	private static void mergeSort(Branch[] src, Branch[] dest,
			int low, int high, int off) {
		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++)
				for (int j = i; j > low
						&& (dest[j - 1]).compareTo(dest[j].getC()) > 0; j--)
					swap(dest, j, j - 1);
			return;
		}

		// Recursively sort halves of dest into src
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >>> 1;
		mergeSort(dest, src, low, mid, -off);
		mergeSort(dest, src, mid, high, -off);

		// If list is already sorted, just copy from src to dest. This
		// is an
		// optimization that results in faster sorts for nearly ordered
		// lists.
		if (src[mid - 1].compareTo(src[mid].getC()) <= 0) {
			System.arraycopy(src, low, dest, destLow, length);
			return;
		}

		// Merge sorted halves (now in src) into dest
		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && src[p].compareTo(src[q].getC()) <= 0)
				dest[i] = src[p++];
			else
				dest[i] = src[q++];
		}
	}

	/**
	 * Swaps x[a] with x[b].
	 */
	private static void swap(Branch[] x, int a, int b) {
		Branch t = x[a];
		x[a] = x[b];
		x[b] = t;
	}

	public static <T> T[] copyOfRange(T[] original, int from, int to) {
		return copyOfRange(original, from, to, (Class<T[]>) original.getClass());
	}

	public static <T, U> T[] copyOfRange(U[] original, int from, int to,
			Class<? extends T[]> newType) {
		int newLength = to - from;
		if (newLength < 0)
			throw new IllegalArgumentException(from + " > " + to);
		T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[newLength]
				: (T[]) Array
						.newInstance(newType.getComponentType(), newLength);
		System.arraycopy(original, from, copy, 0, Math.min(original.length
				- from, newLength));
		return copy;
	}
}