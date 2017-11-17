package com.sjsu.rollbits.sharding.hashing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.datasync.client.CommConnection;
import com.sjsu.rollbits.datasync.server.resources.RollbitsConstants;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.Node;
import com.sjsu.rollbits.yml.Loadyaml;

public class ShardingService {
	private ConsistentHash hash = null;

	// public static ShardingService service;

	protected static Logger logger = LoggerFactory.getLogger("ShardingService");

	protected static AtomicReference<ShardingService> service = new AtomicReference<ShardingService>();

	private ShardingService() {
		List<RNode> list = new ArrayList<>();
		Map<String, Node> nodeMap = ClusterDirectory.getNodeMap();

		if (nodeMap != null) {
			for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
				list.add(new RNode(entry.getKey(), RNode.Type.PRIMARY, entry.getValue().getNodeIp(),
						entry.getValue().getPort()));
			}
			list.add(new RNode(Loadyaml.getProperty("NodeName"), RNode.Type.PRIMARY, Loadyaml.getProperty("NodeIP"),
					Integer.parseInt(Loadyaml.getProperty("NodePort"))));
		} else {
			logger.error("FATAL: Cluster not formed yet, cannot create ring");
			return;
		}
		MurmurHash128 m = new MurmurHash128();
		int virtualNodes = Integer.parseInt(Loadyaml.getProperty(RollbitsConstants.VIRTUAL_NODES));
		int numberOfReplicas = Integer.parseInt(Loadyaml.getProperty(RollbitsConstants.REPLICATION_FACTOR));

		hash = new ConsistentHash(m, numberOfReplicas, list, virtualNodes);
	}

	public static ShardingService getInstance() {
		service.compareAndSet(null, new ShardingService());
		return service.get();

	}

	public static void main(String[] args) {
		ShardingService service = ShardingService.getInstance();

		List<RNode> nodes = service.getNodes(new Message("dsjchskjdgvugdsvks"));

		for (RNode node : nodes) {
			System.out.println(node.getIpAddress());
		}

	}

	public List<RNode> getNodes(Message message) {
		List<RNode> list = new ArrayList<>();
		if (hash != null)
			list = hash.get(message.getUniqueKey());
		return list;
	}

	public void reset() {
		service = null;
		getInstance();
	}

}
