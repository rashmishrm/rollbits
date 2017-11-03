package com.sjsu.rollbits.sharding.hashing;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;


public class ConsistentHash<T> {

	private final HashAlgo hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

	public ConsistentHash(HashAlgo hashFunction, int numberOfReplicas, Collection<T> nodes) {

		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (T node : nodes) {
			add(node);
		}
	}

	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}

	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public T get(Object key) {
		
		System.out.println(circle.size());
		if (circle.isEmpty()) {
			return null;
		}
		Long hash = hashFunction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Long, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

}