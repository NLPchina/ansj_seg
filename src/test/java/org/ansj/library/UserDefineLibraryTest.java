package org.ansj.library;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

public class UserDefineLibraryTest {

	@Test
	public void ambiguityLibraryTest() {

		System.out.println(MyStaticValue.ambiguityLibrary);

		//		李民工作
		//		三个和尚
		//		的确定不
		//		大和尚
		//		张三和
		//		动漫游戏
		//		邓颖超生前

		System.out.println(Arrays.toString(UserDefineLibrary.ambiguityForest.getBranch("习近平").getParam()));
		System.out.println(Arrays.toString(UserDefineLibrary.ambiguityForest.getBranch("李民工作").getParam()));
		System.out.println(Arrays.toString(UserDefineLibrary.ambiguityForest.getBranch("的确定不").getParam()));
		System.out.println(Arrays.toString(UserDefineLibrary.ambiguityForest.getBranch("邓颖超生前").getParam()));
		System.out.println(Arrays.toString(UserDefineLibrary.ambiguityForest.getBranch("三个和尚").getParam()));

	}

	@Test
	public void errTest() {
		UserDefineLibrary.insertWord("汽车车标", "userDefine", 1000);
		System.out.println(DicAnalysis.parse("汽车标").getTerms());//北汽车衣,不锈钢三维汽车标
	}

}
