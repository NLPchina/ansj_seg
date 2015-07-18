package org.ansj.app.web;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.app.summary.Summary;
import org.ansj.app.summary.SummaryComputer;
import org.ansj.app.summary.TagContent;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.BaseAnalysis;
import org.ansj.splitWord.IndexAnalysis;
import org.ansj.splitWord.ToAnalysis;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.ansj.splitWord.NlpAnalysis.nlpParse;

public class AnsjServlet {

    private enum AnsjMethod {
        TO, NLP, BASE, KEYWORD, INDEX, MIN_NLP, SUMMARY
    }

    public static String processRequest(String input, String strMethod, String strNature) throws IOException {
        AnsjMethod method;
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
                terms = nlpParse(input);
                break;
            case MIN_NLP:
                terms = nlpParse(input);
            case KEYWORD:
                KeyWordComputer keyWordComputer = new KeyWordComputer(10);
                keyWords = keyWordComputer.computeArticleTfidf(input);
                break;
            case SUMMARY:
                SummaryComputer sc = new SummaryComputer(null, input);
                Summary summary = sc.toSummary();
                return "<html>" + new TagContent("<font color=\"red\">", "</font>").tagContent(summary) + "...</html>";
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

    private static String keyWordsToString(final Collection<Keyword> keyWords, final boolean nature) {
        final StringBuilder sb = new StringBuilder();
        for (final Keyword k : keyWords) {
            sb.append(k.getName());
            if (nature) {
                sb.append("/").append(k.getScore());
            }
            sb.append("\t");
        }
        return sb.toString();
    }

    private static String termToString(final List<Term> terms, final boolean nature, final AnsjMethod method) {
        if (terms == null) {
            return "Failed to parse input";
        }
        if (nature && method != AnsjMethod.NLP && method != AnsjMethod.MIN_NLP) {
            new NatureRecognition(terms).recognition();
        }
        final StringBuilder sb = new StringBuilder();
        for (final Term term : terms) {
            String tmp = method == AnsjMethod.MIN_NLP && term.getSubTerm() != null ?
                    term.getSubTerm().toString() :
                    term.getName();

            if (nature && !"null".equals(term.getNature().natureStr)) {
                tmp += "/" + term.getNature().natureStr;
            }
            sb.append(tmp).append("\t");
        }
        return sb.toString();
    }
}
