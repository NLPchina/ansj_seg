package org.ansj.app.crf;

import org.junit.Test;

public class ModelTest {

	@Test
	public void test() throws Exception {
		Model model = Model.load("ansj", "src/main/resources/crf.model");
		System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));

		String path = "/Users/sunjian/Documents/src/CRF++-0.58/test/model.txt";

		if (Check.checkFileExit(path)) {
			model = Model.load("ansj", path);
			System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));
		}
		
		path = "/Users/sunjian/Documents/src/Wapiti/test/model.dat";
		if (Check.checkFileExit(path)) {
			model = Model.load("ansj", "/Users/sunjian/Documents/src/Wapiti/test/model.dat");
			System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));
		}
		
	}

}
