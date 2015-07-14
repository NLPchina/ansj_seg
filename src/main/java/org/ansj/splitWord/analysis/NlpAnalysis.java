package org.ansj.splitWord.analysis;

import org.ansj.app.crf.SplitWord;
import org.ansj.dic.LearnTool;
import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.library.DATDictionary;
import org.ansj.library.NatureLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.*;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * 自然语言分词,具有未登录词发现功能。建议在自然语言理解中用。搜索中不要用
 *
 * @author ansj
 */
public class NlpAnalysis extends Analysis {

    private LearnTool learn = null;

    private static final SplitWord DEFAULT_SLITWORD = MyStaticValue.getCRFSplitWord();

    @Override
    protected List<Term> getResult(final Graph graph) {
        return new Merger() {

            @Override
            public List<Term> merger() {
                // TODO Auto-generated method stub
                graph.walkPath();

                // 数字发现
                if (graph.hasNum) {
                    NumRecognition.recognition(graph.terms);
                }

                // 词性标注
                List<Term> result = getResult();
                new NatureRecognition(result).recognition();

                if (learn == null) {
                    learn = new LearnTool();
                }
                learn.learn(graph, DEFAULT_SLITWORD);

                // 通过crf分词
                List<String> words = DEFAULT_SLITWORD.cut(graph.chars);

                for (String word : words) {
                    if (word.length() < 2 || DATDictionary.isInSystemDic(word) || WordAlert.isRuleWord(word)) {
                        continue;
                    }
                    learn.addTerm(new NewWord(word, NatureLibrary.getNature("nw")));
                }

                // 用户自定义词典的识别
                new UserDefineRecognition(graph.terms, forests).recognition();
                graph.rmLittlePath();
                graph.walkPathByScore();

                // 进行新词发现
                new NewWordRecognition(graph.terms, learn).recognition();
                graph.walkPathByScore();

                // 修复人名左右连接
                NameFix.nameAmbiguity(graph.terms);

                // 优化后重新获得最优路径
                result = getResult();

                // 激活辞典
                for (Term term : result) {
                    learn.active(term.getName());
                }

                setRealName(graph, result);

                return result;
            }

            private List<Term> getResult() {
                final List<Term> result = new ArrayList<>();
                int length = graph.terms.length - 1;
                for (int i = 0; i < length; i++) {
                    if (graph.terms[i] == null) {
                        continue;
                    }
                    result.add(graph.terms[i]);
                }
                return result;
            }
        }.merger();
    }

    /**
     * 用户自己定义的词典
     *
     * @param forests forests
     */

    public NlpAnalysis(final List<Forest> forests) {
        this(null, null, forests);
    }

    public NlpAnalysis(final LearnTool learn, final List<Forest> forests) {
        this(null, learn, forests);
    }

    public NlpAnalysis(final Reader reader, final List<Forest> forests) {
        this(reader, null, forests);
    }

    public NlpAnalysis(final Reader reader, final LearnTool learn, final Forest... forests) {
        this(reader, learn, asList(forests));
    }

    public NlpAnalysis(final Reader reader, final LearnTool learn, final List<Forest> forests) {
        super(forests);
        this.learn = learn;
        if (reader != null) {
            super.resetContent(new AnsjReader(reader));
        }
    }

    public static List<Term> parse(final String str, final LearnTool learn, final List<Forest> forests) {
        return new NlpAnalysis(learn, forests).parseStr(str);
    }

    public static List<Term> parse(final String str, final LearnTool learn, final Forest... forests) {
        return parse(str, learn, asList(forests));
    }

    public static List<Term> parse(final String str) {
        return parse(str, null, new Forest[0]);
    }

    public static List<Term> parse(final String str, final List<Forest> forests) {
        return parse(str, null, forests);
    }
}
