package org.ansj.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;

/*
 * 停用词过滤,修正词性到用户词性.必须放到词性识别之后.否则后果自负
 */
public class FilterModifWord {

	public static final String _stop = "_stop";

	private static HashMap<String, String> updateDic = new HashMap<String, String>(0);

	public static void setUpdateDic(HashMap<String, String> updateDic) {
		FilterModifWord.updateDic = updateDic;
	}

	
	
	public static HashMap<String, String> getUpdateDic() {
		return updateDic;
	}



	/**
	 * 停用词.并且更新用户词表.如果是用此方法. 都必须事先设置updateDic.通过setUpdateDic 方法
	 * 
	 * @return
	 */
	public static List<Term> modifResult(List<Term> all) {
		return modifResult(all, updateDic);
	}

	/*
	 * 停用词过滤并且修正词性
	 */
	public static List<Term> modifResult(List<Term> all, HashMap<String, String> updateDic) {
		List<Term> result = new ArrayList<Term>();
		try {
			String natureStr = null;
			for (Term term : all) {
				natureStr = updateDic.get(term.getName());
				if (natureStr == null) {
					result.add(term);
					continue;
				}
				if (!_stop.equals(natureStr)) {
					term.setNature(new Nature(natureStr));
					result.add(term) ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("FilterStopWord.updateDic can not be null , " + "you must use set FilterStopWord.setUpdateDic(map) or use method set map");
		}
		return result;
	}
}
