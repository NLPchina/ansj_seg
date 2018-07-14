package org.ansj.demo;

import org.ansj.library.AmbiguityLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

/**
 * 重新加载用户自定义辞典的两种方式
 * 
 * @author ansj
 * 
 */
public class ReloadAmbiguityLibrary {
	public static void main(String[] args) throws Exception {
		// 从文件中reload
		loadFormFile();
		// 通过内存中reload
		loadFormStr();

		// 歧义辞典增加新词

		Value value = new Value("三个和尚", "三个", "m", "和尚", "n");
		Library.insertWord(AmbiguityLibrary.get(), value);

		// 歧义辞典删除词
		Library.removeWord(AmbiguityLibrary.get(), "三个和尚");

	}

	private static void loadFormStr() {

		Forest forest = AmbiguityLibrary.get();

		//方法1
		Value value = new Value("三个和尚", "三个", "m", "和尚", "n");
		Library.insertWord(forest, value);

		//方法2 
		AmbiguityLibrary.insert(AmbiguityLibrary.DEFAULT, "三个", "m", "和尚", "n");

		// 将新构建的辞典树替换掉旧的。
		AmbiguityLibrary.put(AmbiguityLibrary.DEFAULT, AmbiguityLibrary.DEFAULT, forest);
	}

	private static void loadFormFile() throws Exception {

		// make new forest
		Forest forest = Library.makeForest("new_Library_Path");

		// 将新构建的辞典树替换掉舊的。
		AmbiguityLibrary.put(AmbiguityLibrary.DEFAULT, AmbiguityLibrary.DEFAULT, forest);
	}
}
