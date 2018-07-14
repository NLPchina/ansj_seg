package org.ansj.demo;

import org.ansj.library.DicLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;

import java.io.File;

/**
 * 重新加载用户自定义辞典的两种方式
 * 
 * @author ansj
 * 
 */
public class ReloadUserLibrary {
	public static void main(String[] args) throws Exception {
		// 从文件中reload
		loadFormFile();
		// 通过内存中reload
		loadFormStr();
	}

	private static void loadFormStr() {

		Forest forest = new Forest();

		Library.insertWord(forest, "中国  nature  1000");
		// 将新构建的辞典树替换掉旧的。
		DicLibrary.put(DicLibrary.DEFAULT, DicLibrary.DEFAULT, forest);
	}

	private static void loadFormFile() throws Exception {

		// make new forest
		Forest forest = Library.makeForest(new File("new_Library_Path").getPath());

		// 将新构建的辞典树替换掉舊的。
		DicLibrary.put(DicLibrary.DEFAULT, DicLibrary.DEFAULT, forest);
	}
}
