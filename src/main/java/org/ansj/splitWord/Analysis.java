package org.ansj.splitWord;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.domain.TermNature;
import org.ansj.domain.TermNatures;
import static org.ansj.library.DATDictionary.*;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.impl.GetWordsImpl;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.MyStaticValue;
import org.ansj.util.WordAlert;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 基本分词+人名识别
 * 
 * @author ansj
 * 
 */
public abstract class Analysis {

	/**
	 * 用来记录偏移量
	 */
	public int offe;

	/**
	 * 分词的类
	 */
	private GetWordsImpl gwi = new GetWordsImpl();

	protected Forest[] forests = null;

	private Forest ambiguityForest = UserDefineLibrary.ambiguityForest;

	/**
	 * 文档读取流
	 */
	private AnsjReader br;

	protected Analysis() {
	};

	private LinkedList<Term> terms = new LinkedList<Term>();

	/**
	 * while 循环调用.直到返回为null则分词结束
	 * 
	 * @return
	 * @throws IOException
	 */

	public Term next() throws IOException {
		Term term = null;
		if (!terms.isEmpty()) {
			term = terms.poll();
			term.updateOffe(offe);
			return term;
		}

		String temp = br.readLine();
		offe = br.getStart();
		while (StringUtil.isBlank(temp)) {
			if (temp == null) {
				return null;
			} else {
				temp = br.readLine();
			}

		}

		// 歧异处理字符串

		analysisStr(temp);

		if (!terms.isEmpty()) {
			term = terms.poll();
			term.updateOffe(offe);
			return term;
		}

		return null;
	}

	/**
	 * 一整句话分词,用户设置的歧异优先
	 * 
	 * @param temp
	 */
	private void analysisStr(String temp) {
		Graph gp = new Graph(temp);
		int startOffe = 0;

		if (this.ambiguityForest != null) {
			GetWord gw = new GetWord(this.ambiguityForest, gp.chars);
			String[] params = null;
			while ((gw.getFrontWords()) != null) {
				if (gw.offe > startOffe) {
					analysis(gp, startOffe, gw.offe);
				}
				params = gw.getParams();
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
		List<Term> result = this.getResult(gp);

		terms.addAll(result);
	}

	private void analysis(Graph gp, int startOffe, int endOffe) {
		int start = 0;
		int end = 0;
		char[] chars = gp.chars;

		String str = null;
		char c = 0;
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
				c = chars[start];
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
	 * @param gp
	 * @param result
	 */
	protected void setRealName(Graph graph, List<Term> result) {

		if (!MyStaticValue.isRealName) {
			return;
		}

		String str = graph.realStr;

		for (Term term : result) {
			term.setRealName(str.substring(term.getOffe(), term.getOffe() + term.getName().length()));
		}
	}

	protected List<Term> parseStr(String temp) {
		// TODO Auto-generated method stub
		analysisStr(temp);
		return terms;
	}

	protected abstract List<Term> getResult(Graph graph);

	public abstract class Merger {
		public abstract List<Term> merger();
	}

	/**
	 * 重置分词器
	 * 
	 * @param br
	 */
	public void resetContent(AnsjReader br) {
		this.offe = 0;
		this.br = br;
	}

	public void resetContent(Reader reader) {
		this.offe = 0;
		this.br = new AnsjReader(reader);
	}

	public void resetContent(Reader reader, int buffer) {
		this.offe = 0;
		this.br = new AnsjReader(reader, buffer);
	}

	public Forest getAmbiguityForest() {
		return ambiguityForest;
	}

	public void setAmbiguityForest(Forest ambiguityForest) {
		this.ambiguityForest = ambiguityForest;
	}
}
