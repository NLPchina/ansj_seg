//package org.ansj.recognition;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.ansj.domain.Nature;
//import org.ansj.domain.NewWord;
//import org.ansj.util.Graph;
//
///**
// * 基于规则的新词发现 jijiang feidiao 
// * 
// * @author ansj
// * 
// */
//public class RuleRecognition {
//
//	private static Map<String, String> ruleMap = new HashMap<String, String>();
//
//	static {
//		ruleMap.put("《", "》");
//	}
//
//	public static List<NewWord> recognition(Graph graph) {
//		return recognition(graph.chars);
//	}
//
////	public static List<NewWord> recognition(char[] chars) {
////		String end = null;
////		StringBuilder sb = null;
////		String name;
////		List<NewWord> result = new ArrayList<NewWord>();
////		for (int i = 0; i < chars.length; i++) {
////			name = String.valueOf(chars[i]);
////			if (end == null) {
////				if ((end = ruleMap.get(name)) != null) {
////					sb = new StringBuilder();
////				}
////			} else {
////				if (end.equals(name)) {
////					result.add(new NewWord(sb.toString(), Nature.NATURE_NW, -1000));
////					sb = null;
////					end = null;
////				} else {
////					sb.append(name);
////				}
////			}
////		}
////		return result;
////	}
//}
