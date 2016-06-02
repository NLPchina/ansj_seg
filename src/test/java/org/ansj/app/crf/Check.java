package org.ansj.app.crf;

import java.io.File;
import java.util.logging.Logger;

public class Check {
	private static final Logger LOG = Logger.getLogger("CRF_TEST");

	public static boolean checkFileExit(String path) {
		if (new File(path).exists()) {
			return true;
		} else {
			LOG.warning("check " + path + " not in !");
			return false;
		}
	}
}
