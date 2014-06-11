package org.ansj.demo;

import java.io.File;

import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;

/**
 * 重新加载用户自定义辞典的两种方式
 * 
 * @author ansj
 * 
 */
public class ReloadUserLibrary {
	public static void main(String[] args) {
		// 从文件中reload
		loadFormFile();
		// 通过内存中reload
		loadFormStr();
	}

	private static void loadFormStr() {
		// TODO Auto-generated method stub
		Forest forest = new Forest();

		Library.insertWord(forest, "中国  nature  1000");
		// 将新构建的辞典树替换掉旧的。
		UserDefineLibrary.FOREST = forest;
	}

	private static void loadFormFile() {
		// TODO Auto-generated method stub
		// make new forest
		Forest forest = new Forest();

		UserDefineLibrary.loadFile(forest, new File("new_Library_Path"));

		// 将新构建的辞典树替换掉舊的。
		UserDefineLibrary.FOREST = forest;
	}
}
