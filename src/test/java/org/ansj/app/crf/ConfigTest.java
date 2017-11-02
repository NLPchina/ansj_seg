package org.ansj.app.crf;

import org.ansj.app.crf.pojo.Element;
import org.junit.Test;

import java.util.List;

public class ConfigTest {

	@Test
	public void wordAlertTest() {
		List<Element> wordAlert = Config.wordAlert("超过1亿") ;
		System.out.println(wordAlert);
	}

}
