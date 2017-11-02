package org.ansj.train;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.junit.Test;
import org.nlpcn.commons.lang.util.CollectionUtil;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.MapCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ansj on 28/10/2017.
 */
public class PersonLibraryMaker {

	@Test
	public void split() throws IOException {
		BufferedReader reader = IOUtil.getReader("train_file/corpus/name_freq2.txt", "utf-8");
		String temp = null ;
		MapCount<String> mc = new MapCount<>() ;
		all : while((temp=reader.readLine())!=null){
			String[] split = temp.split("\t");
			String name = split[0].trim() ;
			int freq = Integer.parseInt(split[1]) ;

			for (int i = 0; i < name.length(); i++) {
				if(name.charAt(i)<256 || name.charAt(i)=='？'){
					System.out.println(temp);
					continue all;
				}
			}

			Result parse = BaseAnalysis.parse(name);
			if(parse.size()==1){//单名成词在这里先放弃
//				mc.add("Y\t"+parse.get(0).getName(),freq);
				continue;
			}

			if(name.length()==2){
				mc.add("B\t"+parse.get(0).getName(),freq);
				mc.add("E\t"+parse.get(1).getName(),freq);
				continue;
			}

			if(name.length()==3) {
				if (parse.size() == 2) {
					if (parse.get(0).getName().length() == 1) {
						mc.add("B\t"+parse.get(0).getName(),freq);
						mc.add("Z\t"+parse.get(1).getName(),freq);
					} else if (parse.get(0).getName().length() == 2) {
						mc.add("X\t"+parse.get(0).getName(),freq);
						mc.add("D\t"+parse.get(1).getName(),freq);
					}
				}else{
					mc.add("B\t"+parse.get(0).getName(),freq);
					mc.add("C\t"+parse.get(0).getName(),freq);
					mc.add("D\t"+parse.get(1).getName(),freq);
				}
				continue;
			}

			System.out.println(temp);

		}

		LinkedHashMap<String,Integer> lhm = new LinkedHashMap<>() ;

		List<Map.Entry<String, Double>> entries = CollectionUtil.sortMapByValue(mc.get(), 1);

		for (Map.Entry<String, Double> entry : entries){
			lhm.put(entry.getKey(),entry.getValue().intValue()) ;
		}

		IOUtil.writeMap(lhm,"train_file/dic/person_split.txt","utf-8");
	}

	@Test
	public void merge() throws IOException {
		String[] paths =  new String[]{"train_file/dic/person_split.txt","train_file/dic/person_fix.txt"} ;
		List<String> result = new ArrayList<>() ;
		for (String path : paths){
			result.addAll(IOUtil.readFile2List(path, "utf-8"));
		}
		IOUtil.writeList(result,"src/main/resources/person.dic","utf-8");
	}


}
