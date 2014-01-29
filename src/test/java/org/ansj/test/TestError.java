package org.ansj.test;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

public class TestError {
	
	@Test
	public void test(){
		System.out.println(ToAnalysis.parse("东华能源2012年第四次临时股东大会于2012年11月9日召开"));
	}
}
