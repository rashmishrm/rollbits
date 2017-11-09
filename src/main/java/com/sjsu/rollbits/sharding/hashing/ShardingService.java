package com.sjsu.rollbits.sharding.hashing;

import java.util.ArrayList;
import java.util.List;

public class ShardingService {
	private ConsistentHash hash = null;

	public static ShardingService service;

	private ShardingService() {
		int numberOfReplicas = 3;
		List<RNode> list = new ArrayList<>();
		list.add(new RNode("A", RNode.Type.PRIMARY, "10.0.0.1", 4567));
		list.add(new RNode("B", RNode.Type.PRIMARY, "10.0.0.2", 4567));
		list.add(new RNode("C", RNode.Type.REPLICA, "10.0.0.3", 4567));
		list.add(new RNode("D", RNode.Type.REPLICA, "10.0.0.4", 4567));
		MurmurHash128 m = new MurmurHash128();
		hash = new ConsistentHash(m, numberOfReplicas, list);
	}

	public synchronized static ShardingService getInstance() {

		if (service == null) {

			service = new ShardingService();

		}

		return service;

	}

	public static void main(String[] args) {
		ShardingService service = ShardingService.getInstance();
		List<RNode> nodes = service.getNodes(new Message("86699"));

		for (RNode node : nodes) {
			System.out.println(node.getIpAddress());
		}

	}

	public List<RNode> getNodes(Message message) {
		List<RNode> list = hash.get(message.getUniqueKey());

		// TODO
		// Remove own ip from list

		// List<RNode> list = new ArrayList<>();
		// list.add(new RNode("Node1", RNode.Type.PRIMARY, "10.0.0.2", 4567));
		// list.add(new RNode("Node2", RNode.Type.REPLICA, "10.0.0.3", 4567));
		// list.add(new RNode("Node3", RNode.Type.REPLICA, "10.0.0.4", 4567));

		return list;
	}

}
