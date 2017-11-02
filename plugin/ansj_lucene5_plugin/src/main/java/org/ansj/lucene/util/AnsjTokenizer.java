package org.ansj.lucene.util;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.recognition.impl.SynonymsRecgnition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class AnsjTokenizer extends Tokenizer {
	// 当前词
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	// 偏移量
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	// 距离
	private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);
	// 分词词性
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

	protected Analysis ta = null;

	private LinkedList<Object> result;

	private List<StopRecognition> stops; //停用词对象

	private List<SynonymsRecgnition> synonyms; //同义词词典

	public AnsjTokenizer(Analysis ta, List<StopRecognition> stops, List<SynonymsRecgnition> synonyms) {
		this.ta = ta;
		this.stops = stops;
		this.synonyms = synonyms;
	}
	


	@Override
	public final boolean incrementToken() throws IOException {
		int position = 0;
		if (result == null) {
			parse();
		}

		Object obj = result.pollFirst();
		if (obj == null) {
			result = null;
			return false;
		}

		if (obj instanceof Term) {
			clearAttributes();
			Term term = (Term) obj;
			while (filterTerm(term)) { //停用词
				term = (Term) result.pollFirst();
				if (term == null) {
					result = null;
					return false;
				}
				position++;
			}


			List<String> synonyms = term.getSynonyms(); //获得同义词

			String rName = null;

			if (synonyms != null) {
				for (int i = 1; i < synonyms.size(); i++) {
					result.addFirst(synonyms.get(i));
				}
				rName = synonyms.get(0);
			} else {
				rName = term.getName();
			}
			position++;
			offsetAtt.setOffset(term.getOffe(), term.getOffe() + term.getName().length());
			typeAtt.setType(term.getNatureStr());

			positionAttr.setPositionIncrement(position);
			termAtt.setEmpty().append(rName);

		} else {
			positionAttr.setPositionIncrement(position);
			termAtt.setEmpty().append(obj.toString());
		}

		return true;
	}

	private boolean filterTerm(Term term) {
		if (stops != null && stops.size() > 0) {
			for (StopRecognition filterRecognition : stops) {
				if (filterRecognition.filter(term)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		ta.resetContent(new AnsjReader(this.input));
		parse();
	}

	private void parse() throws IOException {
		Result parse = ta.parse();
		if (synonyms != null) {
			for (SynonymsRecgnition sr : synonyms) {
				parse.recognition(sr);
			}
		}

		result = new LinkedList<Object>(parse.getTerms());
	}

}