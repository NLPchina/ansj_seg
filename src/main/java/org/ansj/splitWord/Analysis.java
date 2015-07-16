package org.ansj.splitWord;

import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.impl.GetWordsImpl;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.ansj.util.WordAlert;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.ansj.library.DATDictionary.IN_SYSTEM;
import static org.ansj.library.DATDictionary.status;
import static org.nlpcn.commons.lang.util.StringUtil.isBlank;

/**
 * 基本分词+人名识别
 *
 * @author ansj
 */
public abstract class Analysis {

    /**
     * 分词的类
     */
    private final GetWordsImpl gwi;

    protected final List<Forest> forests;

    private final LinkedList<Term> terms;

    /**
     * 文档读取流
     */
    private AnsjReader reader;

    /**
     * 用来记录偏移量
     */
    public int offe;

    protected Analysis(final List<Forest> forests) {
        this.gwi = new GetWordsImpl();
        this.forests = Collections.unmodifiableList(forests != null ? forests : new ArrayList<>());
        this.terms = new LinkedList<>();
    }

    /**
     * while 循环调用.直到返回为null则分词结束
     *
     * @return term
     * @throws IOException
     */

    public Term next() throws IOException {
        Term term;
        if (!this.terms.isEmpty()) {
            term = this.terms.poll();
            term.updateOffe(offe);
            return term;
        }

        String temp = this.reader.readLine();
        this.offe = this.reader.getStart();
        while (isBlank(temp)) {
            if (temp == null) {
                return null;
            } else {
                temp = this.reader.readLine();
            }
        }

        // 歧异处理字符串
        analysisStr(temp);
        if (!this.terms.isEmpty()) {
            term = this.terms.poll();
            term.updateOffe(this.offe);
            return term;
        }

        return null;
    }

    /**
     * 一整句话分词,用户设置的歧异优先
     *
     * @param temp temp
     */
    private void analysisStr(final String temp) {
        final Graph gp = new Graph(temp);

        int startOffe = 0;
        final GetWord gw = UserDefineLibrary.getInstance().getWord(gp.chars);
        if (gw != null) {
            String[] params;
            while ((gw.getFrontWords()) != null) {
                if (gw.offe > startOffe) {
                    analysis(gp, startOffe, gw.offe);
                }
                params = (String[]) gw.getParam();
                startOffe = gw.offe;
                for (int i = 0; i < params.length; i += 2) {
                    gp.addTerm(new Term(params[i], startOffe, new TermNatures(new TermNature(params[i + 1], 1))));
                    startOffe += params[i].length();
                }
            }
        }
        if (startOffe < gp.chars.length - 1) {
            analysis(gp, startOffe, gp.chars.length);
        }

        this.terms.addAll(this.getResult(gp));
    }

    private void analysis(final Graph gp, final int startOffe, final int endOffe) {
        final char[] chars = gp.chars;
        int start;
        int end;
        String str;
        for (int i = startOffe; i < endOffe; i++) {
            switch (status(chars[i])) {
                case 0:
                    gp.addTerm(new Term(String.valueOf(chars[i]), i, TermNatures.NULL));
                    break;
                case 4:
                    start = i;
                    end = 1;
                    while (++i < endOffe && status(chars[i]) == 4) {
                        end++;
                    }
                    str = WordAlert.alertEnglish(chars, start, end);
                    gp.addTerm(new Term(str, start, TermNatures.EN));
                    i--;
                    break;
                case 5:
                    start = i;
                    end = 1;
                    while (++i < endOffe && status(chars[i]) == 5) {
                        end++;
                    }
                    str = WordAlert.alertNumber(chars, start, end);
                    gp.addTerm(new Term(str, start, TermNatures.M));
                    i--;
                    break;
                default:
                    start = i;
                    end = i;
                    char c = chars[start];
                    while (IN_SYSTEM[c] > 0) {
                        end++;
                        if (++i >= endOffe)
                            break;
                        c = chars[i];
                    }

                    if (start == end) {
                        gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
                        continue;
                    }

                    gwi.setChars(chars, start, end);
                    while ((str = gwi.allWords()) != null) {
                        gp.addTerm(new Term(str, gwi.offe, gwi.getItem()));
                    }

                    /**
                     * 如果未分出词.以未知字符加入到gp中
                     */
                    if (IN_SYSTEM[c] > 0 || status(c) > 3) {
                        i -= 1;
                    } else {
                        gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
                    }

                    break;
            }
        }
    }

    /**
     * 将为标准化的词语设置到分词中
     *
     * @param graph  graph
     * @param result result
     */
    protected void setRealName(final Graph graph, final List<Term> result) {
        if (!MyStaticValue.isRealName) {
            return;
        }

        final String str = graph.realStr;
        for (final Term term : result) {
            term.setRealName(str.substring(term.getOffe(), term.getOffe() + term.getName().length()));
        }
    }

    protected List<Term> parseStr(final String temp) {
        analysisStr(temp);
        return this.terms;
    }

    protected abstract List<Term> getResult(final Graph graph);

    public abstract class Merger {
        public abstract List<Term> merger();
    }

    /**
     * 重置分词器
     *
     * @param reader reader
     */
    public void resetContent(final AnsjReader reader) {
        this.resetContent(reader, AnsjReader.defaultCharBufferSize);
    }

    public void resetContent(final Reader reader) {
        this.resetContent(reader, AnsjReader.defaultCharBufferSize);
    }

    private void resetContent(final Reader reader, final int buffer) {
        this.offe = 0;
        this.reader = (reader instanceof AnsjReader) ? (AnsjReader) reader : new AnsjReader(reader, buffer);
    }
}
