package org.ansj.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
	/**
	 * map 按照value排序
	 * 
	 * @return
	 */
	public static <K, V> List<Map.Entry<K, V>> sortMapByValue(HashMap<K, V> map,final int sort) {

		List<Map.Entry<K, V>> orderList = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(orderList, new Comparator<Map.Entry<K, V>>() {
			@Override
			@SuppressWarnings("unchecked")
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (((Comparable<V>) o2.getValue()).compareTo(o1.getValue()))*sort;
			}
		});
		return orderList;
	}
}
