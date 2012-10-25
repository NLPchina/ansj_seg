package org.ansj.library.make;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import love.cq.util.IOUtil;

import org.ansj.util.MyStaticValue;

public class MakeLibrary {
	private static String charEncoding = "UTF-8";
	private static String path = "data/library.dic";

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		// englishLibrary() ;
		makeLibrary();
		System.out.println(System.currentTimeMillis() - start);
	}

	
	/**
	 * 重构词典.根据结构体的规则将base数组模型构建出来 base模型的规则首先按顺序排列.其次按hashCode排列
	 * 
	 * @throws IOException
	 */

	public static void makeLibrary() throws Exception {
		BufferedReader reader = IOUtil.getReader(path, charEncoding);
		sortLibrary(reader);

	}

	public static void sortLibrary(BufferedReader reader) throws Exception {
		String line;
		LibraryToTree ltr = new LibraryToTree();
		HashSet<String> keyWords = new HashSet<String>();
		while ((line = reader.readLine()) != null) {
			if (line.contains("#")) {
				continue;
			}
			System.out.println(line);
			keyWords.add(line);
		}
		ltr.addLibrary(keyWords);

		//在内存中生成Branch词典
		List<Branch> head = new ArrayList<Branch>();
		head.add(ltr.head);
		List<Branch> all = treeToLibrary(head, 0, 1);
		//将head移除
		all.remove(0) ;
		MakeArray.makeArray(all) ;
	}


	private static List<Branch> treeToLibrary(List<Branch> all, int begin, int end) {
		int beginNext = end;
		for (int i = begin; i < end; i++) {
			Branch[] branches = all.get(i).branches;
			for (int j = 0; j < branches.length; j++) {
				all.add(branches[j]);
			}
		}
		int endNext = all.size();
		if (begin != end) {
			treeToLibrary(all, beginNext, endNext);
		}
		return all;
	}
}
