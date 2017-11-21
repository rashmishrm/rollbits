package com.sjsu.rollbits.sharding.hashing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.sjsu.rollbits.sharding.hashing.RNode.Type;

public class ConsistentHash {

	private final HashAlgo hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Long, RNode> circle = new TreeMap<Long, RNode>();
	protected static Logger logger = Logger.getLogger("ConsistentHash");

	public ConsistentHash(HashAlgo hashFunction, int numberOfReplicas, Collection<RNode> nodes, int virtualNodes) {

		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;
		for (RNode node : nodes) {
			for (int i = 1; i <= virtualNodes; i++) {

				addNode(node, i);
			}

		}

		logger.info("Formed consistent hashing ring : " + circle);
	}

	public void addNode(RNode node, int id) {

		Long hash = hashFunction.hash(node.toString() + id);
		circle.put(hash, node);

	}

	public void remove(RNode node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public List<RNode> get(Object key) {
		// SortedSet<RNode> list = new TreeSet<>();

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
			if (0 == k) {
				break;
			}

		}

		// return new ArrayList<>(list);
		return list;
	}

}