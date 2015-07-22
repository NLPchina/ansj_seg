package org.ansj.splitWord;

import org.ansj.Term;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.ForeignPersonRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.ansj.AnsjContext.CONTEXT;

/**
 * 标准分词
 *
 * @author ansj
 */
public class ToAnalysis extends Analysis {

    @Override
    protected List<Term> getResult(final Graph graph) {

        graph.walkPath();
        // 数字发现
        if (CONTEXT().numRecognition && graph.hasNum) {
            NumRecognition.recognition(graph.terms);
        }

        // 姓名识别
        if (graph.hasPerson && CONTEXT().nameRecognition) {
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
        return getResult2(graph);
    }

    private void userDefineRecognition(final Graph graph, final List<Forest> forests) {
        new UserDefineRecognition(graph.terms, forests).recognition();
        graph.rmLittlePath();
        graph.walkPathByScore();
    }

    private List<Term> getResult2(final Graph graph) {
        final List<Term> result = new ArrayList<>();
        int length = graph.terms.length - 1;
        for (int i = 0; i < length; i++) {
            if (graph.terms[i] != null) {
                result.add(graph.terms[i]);
            }
        }
        setRealName(graph, result);
        return result;
    }

    /**
     * 用户自己定义的词典
     *
     * @param forests forests
     */
    public ToAnalysis(final Reader reader, final List<Forest> forests) {
        super(forests);
        if (reader != null) {
            super.resetContent(new AnsjReader(reader));
        }
    }

    public static List<Term> parse(final String str, final Forest... forests) {
        return new ToAnalysis(null, asList(forests)).parseStr(str);
    }
}
