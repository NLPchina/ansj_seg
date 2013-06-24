package org.ansj.splitWord;

import static org.ansj.library.InitDictionary.*;
import static org.ansj.library.InitDictionary.status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import love.cq.util.StringUtil;

import org.ansj.domain.Term;
import org.ansj.domain.TermNatures;
import org.ansj.splitWord.impl.GetWordsImpl;
import org.ansj.util.Graph;
import org.ansj.util.WordAlert;

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
	 * 记录上一次文本长度
	 */
	private int tempLength;

	/**
	 * 分词的类
	 */
	private GetWordsImpl gwi = new GetWordsImpl();

	/**
	 * 文档读取流
	 */
	private BufferedReader br;

	/**
	 * 如果文档太过大建议传入输入流
	 * 
	 * @param reader
	 */
	public Analysis(Reader reader) {
		br = new BufferedReader(reader);
	}

	protected Analysis() {
	};

	private LinkedList<Term> terms = new LinkedList<Term>();

	/**
	 * while 循环调用.直到返回为null则分词结束
	 * 
	 * @return
	 * @throws IOException
	 */
	private Term term = null;

	public Term next() throws IOException {

		if (!terms.isEmpty()) {
			term = terms.poll();
			term.updateOffe(offe);
			return term;
		}

		String temp = br.readLine();

		while (StringUtil.isBlank(temp)) {
			if (temp == null) {
				return null;
			} else {
				offe = offe + temp.length() + 1;
				temp = br.readLine();
			}

		}

		offe += tempLength;

		analysis(temp);

		if (!terms.isEmpty()) {
			term = terms.poll();
			term.updateOffe(offe);
			return term;
		}

		return null;
	}

	private void analysis(String temp) {
		// TODO Auto-generated method stub
		Graph gp = new Graph(temp);
		int start = 0;
		int end = 0;
		int length = 0;

		length = temp.length();

		tempLength = length + 1;

		String str = null;
		char c = 0;
		for (int i = 0; i < length; i++) {
			switch (status[conversion(temp.charAt(i))]) {
			case 0:
				gp.addTerm(new Term(temp.charAt(i) + "", i, TermNatures.NULL));
				break;
			case 4:
				start = i;
				end = 1;
				while (++i < length && status[temp.charAt(i)] == 4) {
					end++;
				}
				str = WordAlert.alertEnglish(temp, start, end);
				gp.addTerm(new Term(str, start, TermNatures.EN));
				i--;
				break;
			case 5:
				start = i;
				end = 1;
				while (++i < length && status[temp.charAt(i)] == 5) {
					end++;
				}
				str = WordAlert.alertNumber(temp, start, end);
				gp.addTerm(new Term(str, start, TermNatures.NB));
				i--;
				break;
			default:
				start = i;
				end = i;
				c = temp.charAt(start);
				while (IN_SYSTEM[c] > 0) {
					end++;
					if (++i >= length)
						break;
					c = temp.charAt(i);
				}

				if (start == end) {
					gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
				}

				str = temp.substring(start, end);
				gwi.setStr(str);
				while ((str = gwi.allWords()) != null) {
					gp.addTerm(new Term(str, gwi.offe + start, gwi.getTermNatures()));
				}

				/**
				 * 如果未分出词.以未知字符加入到gp中
				 */
				if (IN_SYSTEM[c] > 0 || status[c] > 3) {
					i -= 1;
				} else {
					gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
				}

				break;
			}
		}
		List<Term> result = this.getResult(gp);

		terms.addAll(result);
	}

	protected List<Term> parseStr(String temp) {
		// TODO Auto-generated method stub
		analysis(temp);
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
	public void resetContent(BufferedReader br) {
		this.br = br;
	}
}
