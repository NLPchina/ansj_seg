package org.ansj.app.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.app.summary.SummaryComputer;
import org.ansj.app.summary.TagContent;
import org.ansj.app.summary.pojo.Summary;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class AnsjServlet {

	private enum AnsjMethod {
		TO, NLP, BASE, KEYWORD, INDEX, MIN_NLP ,SUMMARY
	}

	public static String processRequest(String input, String strMethod, String strNature) throws IOException {
		AnsjMethod method = AnsjMethod.TO;
		if (strMethod != null) {
			method = AnsjMethod.valueOf(strMethod.toUpperCase());
		} else {
			method = AnsjMethod.TO;
		}
		Boolean nature = true;
		if (strNature != null && strNature.toLowerCase().equals("false")) {
			nature = false;
		}
		List<Term> terms = null;
		Collection<Keyword> keyWords = null;
		switch (method) {
		case TO:
			terms = ToAnalysis.parse(input);
			break;
		case NLP:
			terms = NlpAnalysis.parse(input);
			break;
		case MIN_NLP:
			terms = NlpAnalysis.parse(input);
		case KEYWORD:
			KeyWordComputer keyWordComputer = new KeyWordComputer(10);
			keyWords = keyWordComputer.computeArticleTfidf(input);
			break;
		case SUMMARY:
			SummaryComputer sc = new SummaryComputer(null, input) ;
			Summary summary = sc.toSummary() ;
			return "<html>"+new TagContent("<font color=\"red\">", "</font>").tagContent(summary)+"...</html>" ;
		case INDEX:
			terms = IndexAnalysis.parse(input);
			break;
		default:
			terms = BaseAnalysis.parse(input);
		}

		if (terms != null) {
			return termToString(terms, nature, method);
		}

		if (keyWords != null) {
			return keyWordsToString(keyWords, nature);
		}

		return "i am error!";
	}

	private static String keyWordsToString(Collection<Keyword> keyWords, boolean nature) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (Keyword k : keyWords) {
			String tmp = k.getName();
			if (nature) {
				tmp += "/" + k.getScore();
			}
			sb.append(tmp + "\t");
		}
		return sb.toString();
	}

	private static String termToString(List<Term> terms, boolean nature, AnsjMethod method) {
		// TODO Auto-generated method stub
		if (terms == null) {
			return "Failed to parse input";
		}
		if (nature && method != AnsjMethod.NLP && method != AnsjMethod.MIN_NLP) {
			new NatureRecognition(terms).recognition();
		}
		StringBuilder sb = new StringBuilder();
		for (Term term : terms) {
			String tmp = null ;
			if(method == AnsjMethod.MIN_NLP&& term.getSubTerm()!=null){
				tmp = term.getSubTerm().toString() ;
			}else{
				tmp = term.getName();
			}
			
			if (nature && !"null".equals(term.natrue().natureStr)) {
				tmp += "/" + term.natrue().natureStr;
			}
			sb.append(tmp + "\t");
		}
		return sb.toString();
	}

}
