package com.sjsu.rollbits.sharding.hashing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sjsu.rollbits.sharding.hashing.RNode.Type;

public class ConsistentHash {

	private final HashAlgo hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Long, RNode> circle = new TreeMap<Long, RNode>();

	public ConsistentHash(HashAlgo hashFunction, int numberOfReplicas, Collection<RNode> nodes) {

		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		int virtualNodes = 5;

		for (int i = 1; i <= virtualNodes; i++) {
			for (RNode node : nodes) {
				addNode(node, i);
			}

		}

	}

	public void add(RNode node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}

	public void addNode(RNode node, int id) {
		circle.put(hashFunction.hash(node.toString() + id), node);

	}

	public void remove(RNode node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public List<RNode> get(Object key) {
		List<RNode> list = new ArrayList<>();
		System.out.println(circle.size());
		if (circle.isEmpty()) {
			return null;
		}
		Long hash = hashFunction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Long, RNode> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}

		// get next nodes
		SortedMap<Long, RNode> tailMap = new TreeMap<>();
		tailMap.putAll(circle.tailMap(hash));

		if (tailMap.size() < numberOfReplicas) {

			tailMap.putAll(circle.tailMap(circle.firstKey()));
		}

		int k = numberOfReplicas;

		for (Long h : tailMap.keySet()) {
			RNode rnode = null;
			rnode = tailMap.get(h);
			if (k == numberOfReplicas) {

				rnode.setType(Type.PRIMARY);
			} else {
				rnode.setType(Type.REPLICA);
			}

			list.add(tailMap.get(h));
			k--;
			if (k == 0) {
				break;
			}
		}

		return list;
	}

}