package org.ansj.library;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;
import org.nlpcn.commons.lang.dat.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Ansj on 01/12/2017.
 */
public class DATDictionaryTest {

	@Test
	public void write2FileTest() throws IOException {

		ToAnalysis.parse("我爱北京天安门");

		DATDictionary.write2File("core.data");


	}


	@Test
	public void loadTest() throws IOException, ClassNotFoundException {
		long start = System.currentTimeMillis();

		String path = "core.data";

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));

		Item[] items = (Item[]) ois.readObject();


		ois.readObject();

		ois.readObject();



		System.out.println(System.currentTimeMillis() - start);
	}

}
