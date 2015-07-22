package org.ansj.splitWord;

import org.ansj.AnsjContext;
import org.ansj.NewWord;
import org.ansj.Term;
import org.ansj.crf.SplitWord;
import org.ansj.library.CoreDictionary;
import org.ansj.library.NatureLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.ansj.AnsjContext.CONTEXT;

/**
 * 自然语言分词,具有未登录词发现功能。建议在自然语言理解中用。搜索中不要用
 *
 * @author ansj
 */
public class NlpAnalysis extends Analysis {

    private final LearnTool learn;

    /**
     * 用户自己定义的词典
     *
     * @param forests forests
     */
    public NlpAnalysis(final List<Forest> forests, final LearnTool learn, final Reader reader) {
        super(forests);
        this.learn = learn;
        if (reader != null) {
            super.resetContent(new AnsjReader(reader));
        }
    }

    public static List<Term> nlpParse(final LearnTool learn, final String str, final Forest... forests) {
        return new NlpAnalysis(asList(forests), learn, null).parseStr(str);
    }

    public static List<Term> nlpParse(final String str, final Forest... forests) {
        return new NlpAnalysis(asList(forests), null, null).parseStr(str);
    }

    @Override
    protected List<Term> getResult(final Graph graph) {
        final NatureLibrary natureLibrary = AnsjContext.natureLibrary;
        final CoreDictionary dat = CONTEXT().coreDictionary;
        final SplitWord crfSplitWord = CONTEXT().getCrfSplitWord();

        final LearnTool learn = this.learn != null ? this.learn : new LearnTool();

        graph.walkPath();

        // 数字发现
        if (graph.hasNum) {
            NumRecognition.recognition(graph.terms);
        }

        // 词性标注
        List<Term> result = getResult2(graph);
        new NatureRecognition(result).recognition();

        learn.learn(graph, crfSplitWord);

        // 通过crf分词
        List<String> words = crfSplitWord.cut(graph.chars);

        for (String word : words) {
            if (word.length() < 2 || dat.isInSystemDic(word) || WordAlert.isRuleWord(word)) {
                continue;
            }
            learn.addTerm(new NewWord(word, natureLibrary.getNature("nw")), crfSplitWord);
        }

        // 用户自定义词典的识别
        new UserDefineRecognition(graph.terms, forests).recognition();
        graph.rmLittlePath();
        graph.walkPathByScore();

        // 进行新词发现
        new NewWordRecognition(graph.terms, learn.getForest()).recognition();
        graph.walkPathByScore();

        // 修复人名左右连接
        NameFix.fixNameAmbiguity(graph.terms);

        // 优化后重新获得最优路径
        result = getResult2(graph);

        // 激活辞典
        for (Term term : result) {
            learn.active(term.getName());
        }

        setRealName(graph, result);

        return result;
    }

    static List<Term> getResult2(final Graph graph) {
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
}
