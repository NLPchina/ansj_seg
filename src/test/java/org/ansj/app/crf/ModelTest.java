package org.ansj.app.crf;

import junit.framework.Assert;
import org.ansj.library.CrfLibrary;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ModelTest {


    @Test
    public void CRFSplitTest() {
        List<String> cut = CrfLibrary.get().cut("协会主席亚拉·巴洛斯说他们是在1990年开始寻找野生金刚鹦鹉的");
        Set<String> words = new HashSet<String>(cut);
        Assert.assertTrue(words.contains("亚拉·巴洛斯"));
    }

//	@Test
//	public void test() throws Exception {
//		Model model = Model.load("src/main/resources/crf.model");
//		System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));
//
//		String path = "/Users/sunjian/Documents/src/CRF++-0.58/test/model.txt";
//
//		if (Check.checkFileExit(path)) {
//			model = Model.load(path);
//			System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));
//		}
//
//		path = "/Users/sunjian/Documents/src/Wapiti/test/model.dat";
//		if (Check.checkFileExit(path)) {
//			model = Model.load("/Users/sunjian/Documents/src/Wapiti/test/model.dat");
//			System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));
//		}
//
//	}

    @Test
    public void test() throws Exception {
        Model model = Model.load("src/main/resources/crf.model");
        System.out.println(new SplitWord(model).cut("结婚的和尚未结婚的"));
        Assert.assertEquals(new SplitWord(model).cut("结婚的和尚未结婚的").toString(), "[结婚, 的, 和尚, 未, 结婚, 的]");
    }
}
