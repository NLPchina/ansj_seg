package org.ansj.web;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class AnsjServlet {

	private enum AnsjMethod {
		TO, NLP, BASE
	}

	public static String processRequest(String input, String strMethod,
			String strNature) throws IOException {
		AnsjMethod method = AnsjMethod.TO;
		if (strMethod != null) {
			method = AnsjMethod.valueOf(strMethod.toUpperCase());
		} else {
			method = AnsjMethod.TO;
		}
		Boolean nature = true;
		if (strNature.toLowerCase().equals("false")) {
			nature = false;
		}
		List<Term> terms = null;
		switch (method) {
			case TO:
				terms = ToAnalysis.parse(input);
				break;
			case NLP:
				terms = NlpAnalysis.parse(input);
				break;
			default:
				terms = BaseAnalysis.parse(input);

		}
		if (terms == null) {
			return "Failed to parse input";
		}
		if (nature) {
			new NatureRecognition(terms).recognition();
		}
		StringBuilder sb = new StringBuilder();
		for (Term term: terms) {
			String tmp = term.getName();
			if (nature) {
				tmp += "/" + term.getNatrue().natureStr;
			}
			sb.append(tmp + " ");
		}
		return sb.toString();
	}

}
