//package org.ansj.splitWord.analysis;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.Reader;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.ansj.domain.Segement;
//import org.ansj.domain.Term;
//import org.ansj.domain.TermNature;
//import org.ansj.splitWord.Analysis;
//import org.ansj.splitWord.impl.UserDefinedGetWords;
//import org.ansj.util.StringUtil;
//
///**
// * 用户自定义词典+基本分词+人名识别(顺序是先用户自定义词典.因为是正向最大匹配.所以这个不建议增加太多)
// * 
// * @author ansj
// * 
// */
//public class UserDefinedAnalysis implements Analysis {
//
//	/**
//	 * 用户自定义词典
//	 */
//	private UserDefinedGetWords udg = new UserDefinedGetWords();
//
//	/**
//	 * 核心分词词库
//	 */
//	private ToAnalysis toAnalysis = null;
//
//
//	/**
//	 * 文档读取流
//	 */
//	private BufferedReader br;
//
//	/**
//	 * 这个单词的偏移量
//	 */
//	public int offe = 0;
//
//	private int tempLength;
//
//	private LinkedList<Term> terms = new LinkedList<Term>();
//
//	/**
//	 * 如果文档太过大建议传入输入流
//	 * 
//	 * @param reader
//	 */
//	public UserDefinedAnalysis(Reader reader) {
//		br = new BufferedReader(reader);
//	}
//
//	public Term next() throws IOException {
//		// TODO Auto-generated method stub
//		if (!terms.isEmpty()) {
//			return terms.poll();
//		}
//
//		String temp = br.readLine();
//
//		while (StringUtil.isBlank(temp)) {
//			if (temp == null) {
//				return null;
//			} else {
//				offe = offe + temp.length() + 1;
//				temp = br.readLine();
//			}
//
//		}
//
//		offe += tempLength;
//
//		analysis(temp);
//
//		if (!terms.isEmpty()) {
//			return terms.poll();
//		}
//
//		return null;
//	}
//
//	Term term = null;
//
//	private void analysis(String temp) throws IOException {
//		// TODO Auto-generated method stub
//		tempLength = temp.length() + 1;
//		List<Segement> maxFrontWordList = udg.getMaxFrontWordList(temp);
//		Segement segement = null;
//		for (int i = 0; i < maxFrontWordList.size(); i++) {
//			segement = maxFrontWordList.get(i);
//			if (segement.getNatureStr() != null) {
//				terms.add(new Term(segement.getValue(), segement.getOffe() + offe + i, TermNature.USER_DEFINE));
//			} else {
//				toAnalysis = new ToAnalysis(segement.getValue());
//				while ((term = toAnalysis.next()) != null) {
//					term.setOffe(term.getOffe() + offe);
//					terms.add(term);
//				}
//			}
//		}
//	}
//}
