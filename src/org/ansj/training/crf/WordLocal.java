package org.ansj.training.crf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.ansj.library.InitDictionary;

import love.cq.library.Library;
import love.cq.util.IOUtil;

public class WordLocal {
	public static void main(String[] args) throws Exception {
		InitDictionary.initArrays() ;
		BufferedReader reader = IOUtil.getReader("data/crf/prob_emit.txt", "UTF-8") ;
		String temp = null ;
		String[] strs = null ;
		while((temp=reader.readLine())!=null){
			strs = temp.split(":") ;
			if(InitDictionary.words[strs[0].charAt(0)]==null){
				System.out.println(strs[0]);
			}
		}
		
	}
}
