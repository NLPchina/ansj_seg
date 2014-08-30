package org.ansj.splitWord.analysis;

import java.io.IOException;

import org.ansj.splitWord.impl.GetWordsImpl;

public class FastIndexAnalysis {

	public static void main(String[] args) throws IOException {
		String temp = "呵呵，我在项目里面设置了 core.autocrlf=false 在我本地解决了。win下还是要提醒其他人注意下的，跨多个平台开发容易导致提交不必要的变更";

		GetWordsImpl gw = new GetWordsImpl(temp);

		while ((temp = gw.allWords()) != null) {
			System.out.println(temp);
		}
		;


	}

}
