package org.ansj;

import java.io.*;

public class RMRBParse {
	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
				"train_file/corpus/rmrb.txt")));
			 BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(
					 "train_file/corpus/rmrb_new.txt"))) {
			String line = null;
			StringBuilder sb = null;
			int skip = 0;
			while ((line = br.readLine()) != null) {
				sb = parseLine(line);
				String str = sb.toString();
				if (str.contains("[") || str.contains("]")) {
					System.out.println(skip++);
					continue;
				}
				fos.write(str.getBytes());
				fos.write("\n".getBytes());
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static StringBuilder parseLine(String line) {
		StringBuilder sb = null;
		String[] strs = line.split("  ");
		sb = new StringBuilder();
		String word = null;
		for (int i = 0; i < strs.length; i++) {
			String[] split = strs[i].split("\\/");
			word = split[0];
			if (word.length() == 1 && split.length == 2 && split[1].startsWith("nr")&&i!=strs.length-1) {
				sb.append(word);
				split = strs[++i].split("\\/");
				if (split.length == 2 && split[1].startsWith("nr")) {
					sb.append(split[0]+"/nr");
				} else {
					sb.append("/nr") ;
				}
				sb.append("\t");
				continue;
			}
			if (word.startsWith("[")) {
				word = word.replace("[", "");
				sb.append(word);
				if (strs[i].contains("]")) {
					sb.append("\t");
					continue;
				}
				word = strs[++i];

				while (!word.contains("]")) {
					sb.append(word.split("\\/")[0]);
					word = strs[++i];
				}
				sb.append(word.split("\\/")[0]+"/nt" + "\t");
			} else {

				if(split.length==1){
					System.out.println(strs[i]);
					sb.append("[") ;
				}else {
					sb.append(word + "/" + split[1] + "\t");
				}
			}
		}
		return sb;
	}
}
