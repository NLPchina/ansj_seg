package org.ansj.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class AnsjServlet extends HttpServlet {

    private enum AnsjMethod {
        TO, NLP, BASE
    }

    private static final long serialVersionUID = -5835898043864355527L;

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String input = req.getParameter("input");
        String strMethod = req.getParameter("method");
        AnsjMethod method = AnsjMethod.TO;
        resp.setHeader("Content-Type", "text/plain; charset=utf-8");
        if (input == null) {
            resp.sendError(500, "Input not specified");
            return;
        }
        if (strMethod != null) {
            method = AnsjMethod.valueOf(strMethod.toUpperCase());
        }
        List<Term> terms = null;
        switch (method) {
            case TO:
                terms = ToAnalysis.parse(input);
                break;
            case NLP:
                terms = NlpAnalysis.parse(input);
                break;
            case BASE:
                terms = BaseAnalysis.parse(input);
                break;
        }
        if (terms == null) {
            resp.sendError(500, "Failed to parse input");
            return;
        }
        new NatureRecognition(terms).recognition();
        for (Term term: terms) {
            String tmp = term.getName() + "/" + term.getNatrue().natureStr
                    + " ";
            resp.getWriter().append(tmp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.processRequest(req, resp);
    }

}
