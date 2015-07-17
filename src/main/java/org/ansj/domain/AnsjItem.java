package org.ansj.domain;

import java.util.Map;

import org.nlpcn.commons.lang.dat.Item;
import static java.lang.Integer.parseInt;
import static java.lang.Byte.parseByte;

public class AnsjItem extends Item {

	private static final long serialVersionUID = 1L;

	public static final AnsjItem NULL = new AnsjItem();

	public static final AnsjItem BEGIN = new AnsjItem();

	public static final AnsjItem END = new AnsjItem();

	static {
		NULL.base = 0;

		BEGIN.index = 0;
		BEGIN.termNatures = TermNatures.BEGIN;

		END.index = -1;
		END.termNatures = TermNatures.END;
	}

	public String param;

	/**
	 * frequency : 词性词典,以及词性的相关权重
	 */
	public TermNatures termNatures;

	public Map<Integer,Integer> bigramEntryMap;

	@Override
	public void init(String[] split) {
		this.name = split[0];
		this.param = split[1];
	}

	@Override
	public void initValue(final String[] split) {
		this.index = parseInt(split[0]);
        this.base = parseInt(split[2]);
        this.check = parseInt(split[3]);
        this.status = parseByte(split[4]);
		if (this.status > 1) {
            this.name = split[1];
            this.termNatures = new TermNatures(this.index, TermNature.setNatureStrToArray(split[5]));
		}else{
            this.termNatures = new TermNatures(TermNature.NULL);
		}
	}

	@Override
	public String toText() {
		return index + "\t" + name + "\t" + base + "\t" + check + "\t" + status + "\t" + param;
	}
}
