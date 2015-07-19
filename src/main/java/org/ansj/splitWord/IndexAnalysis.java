package org.ansj.splitWord;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.ansj.util.AnsjContext.CONTEXT;

/**
 * 用于检索的分词方式
 *
 * @author ansj
 */
public class IndexAnalysis extends Analysis {

    // 构建最优路径
    @Override
    protected List<Term> getResult(final Graph graph) {
        graph.walkPath();

        // 数字发现
        if (CONTEXT().isNumRecognition && graph.hasNum) {
            NumRecognition.recognition(graph.terms);
        }

        // 姓名识别
        if (graph.hasPerson && CONTEXT().isNameRecognition) {
            // 亚洲人名识别
            new AsianPersonRecognition(graph.terms).recognition();
            graph.walkPathByScore();
            NameFix.fixNameAmbiguity(graph.terms);
            // 外国人名识别
            new ForeignPersonRecognition(graph.terms).recognition();
            graph.walkPathByScore();
        }

        // 用户自定义词典的识别
        userDefineRecognition(graph, forests);

        return result(graph);
    }

    static void userDefineRecognition(final Graph graph, final List<Forest> forests) {
        new UserDefineRecognition(graph.terms, forests).recognition();
        graph.rmLittlePath();
        graph.walkPathByScore();
    }

    /**
     * 检索的分词
     */
    List<Term> result(final Graph graph) {
        String temp;

        final List<Term> result = new LinkedList<>();
        int length = graph.terms.length - 1;
        for (int i = 0; i < length; i++) {
            if (graph.terms[i] != null) {
                result.add(graph.terms[i]);
            }
        }

        final LinkedList<Term> last = new LinkedList<>();
        for (Term term : result) {
            if (term.getName().length() >= 3) {
                GetWordsImpl gwi = new GetWordsImpl(term.getName());
                while ((temp = gwi.allWords()) != null) {
                    if (temp.length() < term.getName().length() && temp.length() > 1) {
                        last.add(new Term(temp, gwi.offe + term.getOffe(), TermNatures.NULL));
                    }
                }
            }
        }

        result.addAll(last);

        setRealName(graph, result);
        return result;
    }

    public IndexAnalysis(final Reader reader, final List<Forest> forests) {
        super(forests);
        if (reader != null) {
            super.resetContent(new AnsjReader(reader));
        }
    }

    public IndexAnalysis(final Reader reader, final Forest... forests) {
        this(reader, asList(forests));
    }

    public IndexAnalysis(final List<Forest> forests) {
        this(null, forests);
    }

    public static List<Term> parse(final String str) {
        return parse(str, null);
    }

    public static List<Term> parse(final String str, final List<Forest> forests) {
        return new IndexAnalysis(forests).parseStr(str);
    }
}
