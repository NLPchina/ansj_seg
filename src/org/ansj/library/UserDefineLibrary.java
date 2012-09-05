package org.ansj.library;

import java.io.BufferedReader;
import java.util.ArrayList;

import love.cq.domain.Forest;
import love.cq.library.Library;
import love.cq.util.IOUtil;

import org.ansj.util.MyStaticValue;
import org.ansj.util.StringUtil;

public class UserDefineLibrary {
	public static Forest FOREST = null;
	static {
		try {
			long start = System.currentTimeMillis();
			FOREST = new Forest();

			BufferedReader br = MyStaticValue.getUserDefineReader();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				} else {
					Library.insertWord(FOREST, temp);
				}
			}

			if ((temp = MyStaticValue.rb.getString("userLibrary")) != null) {
				br = IOUtil.getReader(temp, "UTF-8");
				while ((temp = br.readLine()) != null) {
					if (StringUtil.isBlank(temp)) {
						continue;
					} else {
						Library.insertWord(FOREST, temp);
					}
				}
			}
			System.out.println("加载用户自定义词典完成用时:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("加载用户自定义词典加载失败:");
		}
	}

}
