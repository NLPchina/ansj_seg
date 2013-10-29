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

	public static String processRequest(String input, String strMethod) throws IOException {
		AnsjMethod method = AnsjMethod.TO;
		if (strMethod != null) {
			method = AnsjMethod.valueOf(strMethod.toUpperCase());
		} else {
			method = AnsjMethod.TO;
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
		new NatureRecognition(terms).recognition();
		StringBuilder sb = new StringBuilder();
		for (Term term : terms) {
			sb.append(term.getName() + "/" + term.getNatrue().natureStr + " ");
		}
		return sb.toString();
	}

}
