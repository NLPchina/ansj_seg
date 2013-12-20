package org.ansj.app.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class AnsjServlet {

    private enum AnsjMethod {
        TO, NLP, BASE, KEYWORD
    }

    public static String processRequest(String input, String strMethod, String strNature)
                                                                                         throws IOException {
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
            case KEYWORD:
                KeyWordComputer keyWordComputer = new KeyWordComputer(10);
                keyWords = keyWordComputer.computeArticleTfidf(input);
                break;
            default:
                terms = BaseAnalysis.parse(input);
        }

        if (terms != null) {
            return termToString(terms, nature);
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

    private static String termToString(List<Term> terms, boolean nature) {
        // TODO Auto-generated method stub
        if (terms == null) {
            return "Failed to parse input";
        }
        if (nature) {
            new NatureRecognition(terms).recognition();
        }
        StringBuilder sb = new StringBuilder();
        for (Term term : terms) {
            String tmp = term.getName();
            if (nature) {
                tmp += "/" + term.getNatrue().natureStr;

                if (term.getNatrue().natureStr == null || term.getNatrue().natureStr.equals("null")) {
                    continue;
                }
            }
            sb.append(tmp + "\t");
        }
        return sb.toString();
    }

}
