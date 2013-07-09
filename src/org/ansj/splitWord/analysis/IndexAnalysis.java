package org.ansj.splitWord.analysis;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.AsianPersonRecognition;
import org.ansj.recognition.NumRecognition;
import org.ansj.recognition.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.Graph;

/**
 * 用于检索的分词方式
 * 
 * @author ansj
 * 
 */
public class IndexAnalysis extends Analysis {

	public IndexAnalysis(Reader reader) {
		super(reader);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<Term> getResult(final Graph graph) {
		Merger merger = new Merger() {

			@Override
			public List<Term> merger() {
				graph.walkPath();

				// 数字发现
				NumRecognition.recognition(graph.terms);

				// 姓名识别
				new AsianPersonRecognition(graph.terms).recognition();

				
				// 用户自定义词典的识别
				new UserDefineRecognition(graph.terms).recognition();

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
				String temp = null ;
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					term = graph.terms[i];
					while (term != null) {
						all.add(term);
						temp = term.getName() ;
						term = term.getNext();
						if (term == null || term.getName().length() == 1||temp.equals(term.getName())) {
							break;
						}
						
					}
				}
				return all;
			}
		};

		return merger.merger();
	}

	private IndexAnalysis() {
	};

	public static List<Term> parse(String str) {
		return new IndexAnalysis().parseStr(str);
	}
}
