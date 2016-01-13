package org.ansj.splitWord.analysis;

import java.io.IOException;

import org.ansj.library.UserDefineLibrary;
import org.junit.Test;

public class UserDefineAnalysisTest {

	@Test
	public void test() throws IOException {
		UserDefineLibrary.insertWord("金水区", "ad", 1000);
		UserDefineLibrary.insertWord("渝北区", "ad", 1000);
		UserDefineLibrary.insertWord("来自大", "ab", 3000);
		UserDefineLibrary.insertWord("自大学", "ab", 1000);

//		System.out.println(UserDefineAnalysis.parse("重庆重庆市渝北区金童路奥山别墅1-6-2"));
//		System.out.println(UserDefineAnalysis.parse("河南省郑州市金水区金水区农科路与文博西路交叉口向东２００米路南"));
		System.out.println(UserDefineAnalysis.parse("来自大学生小说网"));
	}

}
