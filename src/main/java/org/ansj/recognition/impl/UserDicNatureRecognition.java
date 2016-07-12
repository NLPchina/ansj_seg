package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.Recognition;

public class UserDicNatureRecognition implements Recognition {

	@Override
	public void recognition(Result result) {
		for (Term term : result) {
			String[] params = UserDefineLibrary.getParams(term.getName());
			if (params != null) {
				term.setNature(new Nature(params[0]));
			}
		}
	}

}
