package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import love.cq.util.StringUtil;

import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.dic.LearnTool;
import org.ansj.domain.NewWord;
import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.library.InitDictionary;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.NatureRecognition;
import org.ansj.recognition.NewWordRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;

/**
 * 自然语言分词,具有未登录词发现功能。建议在自然语言理解中用。搜索中不要用
 * 
 * @author ansj
 * 
 */
public class NlpAnalysis extends Analysis {

    private LearnTool learn = null;

    private Model model = null;

    private SplitWord sw = null;

    public NlpAnalysis(Reader reader, Model model, LearnTool learn) {
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
     * 
     * @param reader
     * @param modelPath
     * @param templatePath
     */
    private NlpAnalysis(Model model, LearnTool learn) {
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
	if (sw == null) {
	    sw = new SplitWord(model);
	}

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

		learn.learn(graph);

		// 用户自定义词典的识别
		new UserDefineRecognition(graph.terms).recognition();
		graph.walkPathByScore();

		// 通过crf分词
		List<String> words = sw.cut(graph.str);

		for (String word : words) {
		    if (word.length() == 1 || word.endsWith(".") || StringUtil.isBlank(word)) {
			continue;
		    }
		    if (InitDictionary.isInSystemDic(word) || UserDefineLibrary.contains(word)) {
			continue;
		    }

		    if (word.charAt(0) < 256 || word.charAt(word.length() - 1) < 256) {
			continue;
		    }
		    learn.addTerm(new NewWord(word, TermNatures.NW, -1, word.length()));
		}
		// 进行新词发现
		new NewWordRecognition(graph.terms, learn).recognition();
		graph.rmLittlePathByScore();
		graph.walkPathByScore();

		// 优化后重新获得最优路径
		result = getResult();

		return result;
	    }

	    private List<Term> getResult() {
		// TODO Auto-generated method stub
		List<Term> result = new ArrayList<Term>();
		int length = graph.terms.length - 1;
		for (int i = 0; i < length; i++) {
		    if (graph.terms[i] == null) {
			continue;
		    }
		    result.add(graph.terms[i]);
		}
		return result;
	    }
	};
	return merger.merger();
    }

    public static List<Term> parse(String str, Model model) {
	return new NlpAnalysis(model, null).parseStr(str);
    }

    public static List<Term> parse(String str, LearnTool learn) {
	return new NlpAnalysis(null, learn).parseStr(str);
    }

    public static List<Term> parse(String str) {
	return new NlpAnalysis(null, null).parseStr(str);
    }

    public static List<Term> parse(String str, Model model, LearnTool learn) {
	return new NlpAnalysis(model, learn).parseStr(str);
    }

}
