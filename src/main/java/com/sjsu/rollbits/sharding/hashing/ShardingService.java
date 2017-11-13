package com.sjsu.rollbits.sharding.hashing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sjsu.rollbits.client.serverdiscovery.MyConstants;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.Node;

public class ShardingService {
	private ConsistentHash hash = null;

	public static ShardingService service;

	private ShardingService() {
		int numberOfReplicas = 2;
		List<RNode> list = new ArrayList<>();
		Map<String, Node> nodeMap = ClusterDirectory.getNodeMap();
		for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
			list.add(new RNode(entry.getKey(), RNode.Type.PRIMARY, entry.getValue().getNodeIp(),
					entry.getValue().getPort()));
		}
		list.add(new RNode(MyConstants.NODE_NAME, RNode.Type.PRIMARY, MyConstants.NODE_IP,
				Integer.parseInt(MyConstants.NODE_PORT)));

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
		List<RNode> nodes = service.getNodes(new Message("nishantrathi"));

		for (RNode node : nodes) {
			System.out.println(node.getIpAddress());
		}

	}

	public List<RNode> getNodes(Message message) {
		List<RNode> list = hash.get(message.getUniqueKey());
		System.out.println(list);

		// TODO
		// Remove own ip from list

		// List<RNode> list = new ArrayList<>();
		// list.add(new RNode("Node1", RNode.Type.PRIMARY, "10.0.0.2", 4567));
		// list.add(new RNode("Node2", RNode.Type.REPLICA, "10.0.0.3", 4567));
		// list.add(new RNode("Node3", RNode.Type.REPLICA, "10.0.0.4", 4567));

		return list;
	}

}
