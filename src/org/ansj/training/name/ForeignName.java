package org.ansj.training.name;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.ForeignPersonRecognition;

import love.cq.util.IOUtil;

public class ForeignName {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = IOUtil.getReader("./data/name/foreign_name.dic", "UTF-8");
		String temp = null;
		HashSet<String> hs = new HashSet<String>();
		String name = null;
		while ((temp = reader.readLine()) != null) {
			List<Term> paser = ToAnalysis.paser(temp);
			for (int i = 0; i < paser.size(); i++) {
				name = paser.get(i).getName();
				if(name.length()>1){
					if(!ForeignPersonRecognition.isFName(name)&&paser.size()>1){
						System.out.println(name);
					}
				}
			}
		}
	}

	public static class Entry implements Serializable {

		private static final long serialVersionUID = 1L;

		// 是否是外国人名 1.任意位置. 2.词前 3.词中 4.词尾
		int[] local = new int[3];

		public void addIndex(int index) {
			local[index]++;
		}

		public String toString() {
			return local[0] + "\t" + local[1] + "\t" + local[2];
		}
	}
}
