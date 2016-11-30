package org.ansj.dic.impl;

import java.io.InputStream;

import org.ansj.dic.DicReader;
import org.ansj.dic.PathToStream;

/**
 * 从系统jar包中读取文件，你们不能用，只有我能用 jar://
 * 
 * @author ansj
 *
 */
public class Jar2Stream extends PathToStream {

	@Override
	public InputStream toStream(String path) {
		return DicReader.getInputStream(path.substring(6));
	}

}
