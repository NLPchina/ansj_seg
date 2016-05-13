package org.ansj.lucene.util;

import java.io.IOException;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public final class AnsjTokenizer extends Tokenizer {
	// 当前词
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	// 偏移量
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	// 距离
	private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);
	// 分词词性
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

	private int skippedPositions;

	protected Analysis ta = null;
	/** 自定义停用词 */
	private Set<String> filter;

	public AnsjTokenizer(Analysis ta, Set<String> filter) {
		this.ta = ta;
		this.filter = filter;
	}

	public AnsjTokenizer(Analysis ta) {
		this.ta = ta;
	}

	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();

		skippedPositions = 0;

		int position = 0;
		Term term = null;
		String name = null;
		int length = 0;
		boolean flag = true;
		do {
			term = ta.next();
			if (term == null) {
				break;
			}

			name = term.getName();
			length = name.length();

			if (filter != null && filter.contains(name)) {
				continue;
			} else {
				position++;
				flag = false;
			}
		} while (flag);
		if (term != null) {
			positionAttr.setPositionIncrement(position);
			termAtt.setEmpty().append(term.getName());
			offsetAtt.setOffset(term.getOffe(), term.getOffe() + length);
			typeAtt.setType(term.getNatureStr());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		ta.resetContent(new AnsjReader(this.input));
		skippedPositions = 0;
	}

}