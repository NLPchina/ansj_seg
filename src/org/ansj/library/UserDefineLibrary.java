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
			FOREST = new Forest() ; 
					//Library.makeForest();
			BufferedReader br = IOUtil.getReader(MyStaticValue.rb.getString("userLibrary"), "UTF-8") ;
			String temp = null ;
			while((temp=br.readLine())!=null){
				if(StringUtil.isBlank(temp)) continue ;
				
				String[] strs = temp.split("\t") ;
				
				/**
				 * 排除用户词典和核心词典重复的词
				 */
//				if(!InitDictionary.isInSystemDic(strs[0])){
					Library.insertWord(FOREST, temp) ;
//				}
			}
			System.out.println("加载用户自定义词典完成用时:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
