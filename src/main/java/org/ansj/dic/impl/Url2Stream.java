package org.ansj.dic.impl;

import java.io.InputStream;

import org.ansj.dic.PathToStream;
import org.nutz.http.Http;
import org.nutz.http.Response;

/**
 * url://http://maven.nlpcn.org/down/library/default.dic
 * 
 * @author ansj
 *
 */
public class Url2Stream extends PathToStream {

	@Override
	public InputStream toStream(String path) {
		path = path.substring(6);
		Response response = Http.get(path);
		return response.getStream();
	}

}
