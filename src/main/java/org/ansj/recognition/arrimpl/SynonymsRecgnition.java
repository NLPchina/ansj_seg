package org.ansj.recognition.arrimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 同义词功能
 * 
 * @author Ansj
 *
 */
public class SynonymsRecgnition {

	private static final SmartForest<String[]> defaultSynonymsLibrary = new SmartForest<>();

	static {

		String synonymsLibrary = MyStaticValue.synonymsLibrary;

		if (synonymsLibrary == null || !new File(synonymsLibrary).exists()) {
			MyStaticValue.LIBRARYLOG.warn(synonymsLibrary + " not exists so set syn to empty!");
		} else {
			try (BufferedReader reader = IOUtil.getReader(synonymsLibrary, IOUtil.UTF8)) {
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					if (StringUtil.isBlank(temp)) {
						continue;
					}
					String[] split = temp.split("\t");
					if (split.length <= 1) {
						MyStaticValue.LIBRARYLOG.warn(temp + " in synonymsLibrary not in to library !");
						continue;
					}

					for (String word : split) {
						defaultSynonymsLibrary.add(word, split);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
