package org.ansj.recognition.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于规则的新词发现 jijiang feidiao
 * 
 * @author ansj
 * 
 */
public class BookRecognition extends PairRecognition {

	private static final long serialVersionUID = 1L;

	private static Map<String, String> ruleMap = new HashMap<String, String>();

	static {
		ruleMap.put("《", "》");
	}

	public BookRecognition() {
		super("book", ruleMap);
	}
}
