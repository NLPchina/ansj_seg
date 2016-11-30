package org.ansj.domain;

public class KV<K, V> {

	private K k;

	private V v;

	private <K,V> KV<K,V>(K k, V v) {
		this.k = k;
		this.v = v;
	}

	public static <K, V> KV<K, V> with(K k, V v) {
		return new KV(k, v);
	}
}
