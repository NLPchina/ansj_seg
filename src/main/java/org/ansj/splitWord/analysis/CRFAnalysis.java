package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import love.cq.util.StringUtil;

import org.ansj.app.crf.SplitWord;
import org.ansj.app.crf.model.Model;
import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;

/**
 * 自然语言分词,结果不稳定但是很全面
 * 
 * @author ansj
 * 
 */
public class CRFAnalysis extends Analysis {

    private LearnTool learn = null;

    private Model model = null;

    public CRFAnalysis(Reader reader, Model model, LearnTool learn) {
        super(reader);
        if (model == null) {
            this.model = MyStaticValue.getDefaultModel();
        } else {
            this.model = model;
        }
        if (learn == null) {
            this.learn = new LearnTool();
        } else {
            this.learn = learn;
        }
    }

    /**
     * 用户自定义的model，
     * @param reader
     * @param modelPath
     * @param templatePath
     */
    private CRFAnalysis(Model model, LearnTool learn) {
        if (model == null) {
            this.model = MyStaticValue.getDefaultModel();
        } else {
            this.model = model;
        }
        if (learn == null) {
            this.learn = new LearnTool();
        } else {
            this.learn = learn;
        }
    }

    @Override
    protected List<Term> getResult(final Graph graph) {
        // TODO Auto-generated method stub
        Merger merger = new Merger() {
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

                SplitWord sw = new SplitWord(model);

                learn.learn(graph);

                List<String> words = sw.cut(graph.str);

                for (String word : words) {
                    if (word.length() == 1 || word.endsWith(".")||StringUtil.isBlank(word)) {
                        continue;
                    }
                    learn.addTerm(new NewWord(word, TermNatures.NW, -word.length(), word.length()));
                }
                // 用户自定义词典的识别
                new UserDefineRecognition(graph.terms).recognition();
                graph.walkPathByScore();

                // 进行新词发现
                new NewWordRecognition(graph.terms, learn).recognition();
                graph.walkPathByScore();

                //优化后重新获得最优路径
                result = getResult();

                return result;
            }

            private List<Term> getResult() {
                // TODO Auto-generated method stub
                List<Term> result = new ArrayList<Term>();
                int length = graph.terms.length - 1;
                for (int i = 0; i < length; i++) {
                    if (graph.terms[i] != null) {
                        result.add(graph.terms[i]);
                    }
                }
                return result;
            }
        };
        return merger.merger();
    }

    public static List<Term> parse(String str, Model model) {
        return new CRFAnalysis(model, null).parseStr(str);
    }

    public static List<Term> parse(String str, LearnTool learn) {
        return new CRFAnalysis(null, learn).parseStr(str);
    }

    public static List<Term> parse(String str) {
        return new CRFAnalysis(null, null).parseStr(str);
    }

    public static List<Term> parse(String str, Model model, LearnTool learn) {
        return new CRFAnalysis(model, learn).parseStr(str);
    }

}
