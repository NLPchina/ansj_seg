package org.ansj.library;

import java.io.BufferedReader;
import java.io.File;

import love.cq.domain.Forest;
import love.cq.library.Library;
import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

import org.ansj.util.MyStaticValue;

public class UserDefineLibrary {
	public static Forest FOREST = null;
	static {
		try {
			long start = System.currentTimeMillis();
			FOREST = new Forest();

			//先加载系统内置补充词典
			BufferedReader br = MyStaticValue.getUserDefineReader();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (StringUtil.isBlank(temp)) {
					continue;
				} else {
					Library.insertWord(FOREST, temp);
				}
			}

			//加载用户自定义词典
			if ((temp = MyStaticValue.rb.getString("userLibrary")) != null&&new File(temp).isFile()) {
				br = IOUtil.getReader(temp, "UTF-8");
				while ((temp = br.readLine()) != null) {
					if (StringUtil.isBlank(temp)) {
						continue;
					} else {
						Library.insertWord(FOREST, temp);
					}
				}
			}else{
				System.err.println("用户自定义词典:"+temp+", 没有这个文件!");
			}
			System.out.println("加载用户自定义词典完成用时:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("加载用户自定义词典加载失败:");
		}
	}

}
