package org.ansj.demo;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.model.WapitiCRFModel;
import org.ansj.util.MyStaticValue;

public class WapitiModelDemo {
	public static void main(String[] args) throws Exception {
		
		Model model = WapitiCRFModel.parseFileToModel("/Users/sunjian/Documents/src/Wapiti/test/model.dat", "/Users/sunjian/Documents/src/Wapiti/test/pattern.txt", 0) ;
		
		
	}
}
