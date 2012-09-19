package org.ansj.domain;

import java.io.Serializable;

/**
 * google语义模型用到的词与词之间的关联频率
 * 
 * @author ansj
 * 
 */

public class BigramEntry implements Comparable<BigramEntry>, Serializable {
	private static final long serialVersionUID = 5994821814571715244L;

	public int id;
	public int freq;

	public BigramEntry(int id, int freq) {
		this.id = id;
		this.freq = freq;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.id == obj.hashCode();
	}

	@Override
	public int compareTo(BigramEntry o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}

}
