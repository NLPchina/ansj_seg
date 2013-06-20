package org.ansj.demo;

import java.util.HashMap;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;

import love.cq.domain.Forest;

/**
 * 这个例子是根据不同的,用户自定义词典来加载分词器
 * @author ansj
 *
 */
public class UserDefineAnalysisDemo {
	private static HashMap<String,Forest> userForestMap = new HashMap<String,Forest>() ;
	
	public static void main(String[] args) {
		//创建一些用户词典结构树
		Forest forest = UserDefineLibrary.makeUserDefineForest(false, "library/user1.dic") ;
		userForestMap.put("user1", forest) ;
		forest = UserDefineLibrary.makeUserDefineForest(false, "library/user2.dic") ;
		userForestMap.put("user2", forest) ;
		
		List<Term> paser = null ;
		paser = ToAnalysis.parse("java学习是一个很难的过程.", userForestMap.get("user1")) ;
		System.out.println(paser);
		paser = ToAnalysis.parse("java学习是一个很难的过程.", userForestMap.get("user2")) ;
		System.out.println(paser);
		paser = ToAnalysis.parse("php学习是一个很难的过程.", userForestMap.get("user2")) ;
		System.out.println(paser);
		
	}
}
