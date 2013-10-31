package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import love.cq.domain.Forest;

import org.ansj.domain.Term;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;

/**
 * 用于检索的分词方式
 * 
 * @author ansj
 * 
 */
public class IndexAnalysis extends Analysis {
    private Forest[] forests = null;

    public IndexAnalysis(Reader reader) {
        super(reader);
        // TODO Auto-generated constructor stub
    }

    private IndexAnalysis() {
    };

    public IndexAnalysis(Forest[] forests) {
        // TODO Auto-generated constructor stub
        this.forests = forests;
    }

    @Override
    protected List<Term> getResult(final Graph graph) {
        Merger merger = new Merger() {

            @Override
            public List<Term> merger() {
                graph.walkPath();

                // 数字发现
                if (MyStaticValue.isNumRecognition)
                    NumRecognition.recognition(graph.terms);

                // 姓名识别
                if (MyStaticValue.isNameRecognition)
                    new AsianPersonRecognition(graph.terms).recognition();

                // 用户自定义词典的识别
                if (forests == null) {
                    new UserDefineRecognition(graph.terms).recognition();
                } else {
                    for (Forest forest : forests) {
                        if (forest == null)
                            continue;
                        new UserDefineRecognition(graph.terms, forest).recognition();
                    }
                }

                return result();
            }

            /**
             * 检索的分词
             * 
             * @return
             */
            private List<Term> result() {
                // TODO Auto-generated method stub
                List<Term> all = new LinkedList<Term>();
                Term term = null;
                String temp = null;
                int length = graph.terms.length - 1;
                for (int i = 0; i < length; i++) {
                    term = graph.terms[i];
                    while (term != null) {
                        all.add(term);
                        temp = term.getName();
                        term = term.getNext();
                        if (term == null || term.getName().length() == 1
                            || temp.equals(term.getName())) {
                            break;
                        }

                    }
                }
                return all;
            }
        };

        return merger.merger();
    }

    public static List<Term> parse(String str) {
        return new IndexAnalysis().parseStr(str);
    }

    public static List<Term> parse(String str, Forest... forests) {
        return new IndexAnalysis(forests).parseStr(str);

    }
}
