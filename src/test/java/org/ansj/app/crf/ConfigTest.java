package org.ansj.app.crf;

import static org.junit.Assert.*;

import java.util.List;

import org.ansj.app.crf.pojo.Element;
import org.junit.Test;

public class ConfigTest {

	@Test
	public void wordAlertTest() {
		List<Element> wordAlert = Config.wordAlert("超过1亿") ;
		System.out.println(wordAlert);
	}

}
