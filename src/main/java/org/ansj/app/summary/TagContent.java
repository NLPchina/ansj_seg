package org.ansj.app.summary;

import java.util.List;

import love.cq.domain.SmartForest;
import love.cq.splitWord.SmartGetWord;

import org.ansj.app.summary.pojo.Summary;

/**
 * 关键字标红，
 * 
 * @author ansj
 * 
 */
public class TagContent {

	private String beginTag, endTag;

	public TagContent(String beginTag, String endTag) {
		this.beginTag = beginTag;
		this.endTag = endTag;
	}

	public String tagContent(Summary summary) {
		return tagContent(summary.getKeyWords(), summary.getSummary());
	}

	public String tagContent(List<String> keyWords, String content) {
		// TODO Auto-generated method stub
		SmartForest<Integer> sf = new SmartForest<Integer>();
		for (String keyWord : keyWords) {
			sf.add(keyWord.toLowerCase(), 1);
		}

		SmartGetWord<Integer> sgw = new SmartGetWord<Integer>(sf, content.toLowerCase());

		int beginOffe = 0;
		String temp = null;
		StringBuilder sb = new StringBuilder();
		while ((temp = sgw.getFrontWords()) != null) {
			System.out.println(temp+"\t"+beginOffe+"\t"+sgw.offe);
			sb.append(content.substring(beginOffe, sgw.offe));
			sb.append(beginTag);
			sb.append(content.substring(sgw.offe, sgw.offe + temp.length()));
			sb.append(endTag);
			System.out.println(sb);
			beginOffe = sgw.offe + temp.length();
		}

		if (beginOffe < content.length() - 1) {
			sb.append(content.substring(beginOffe, content.length()));
		}

		return sb.toString();
	}

}
