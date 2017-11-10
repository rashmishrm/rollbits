package com.sjsu.rollbits.discovery;

import java.util.HashMap;
import java.util.Map;

import routing.Pipe.NetworkDiscoveryPacket;

public class ClusterDirectory {

	public static Map<String, Map<String, Node>> clusterMap = new HashMap<String, Map<String, Node>>();

	public static void addToDirectory(NetworkDiscoveryPacket request) {
		if (clusterMap.containsKey(request.getGroupTag())) {
			Map<String, Node> nMap = clusterMap.get(request.getGroupTag());
			nMap.put(request.getNodeId(), new Node(request.getNodeId(), request.getNodeAddress(), request.getNodePort(),
					request.getGroupTag(), request.getSender()));
		} else {
			Map<String, Node> nMap = new HashMap<>();
			nMap.put(request.getNodeId(), new Node(request.getNodeId(), request.getNodeAddress(), request.getNodePort(),
					request.getGroupTag(), request.getSender()));
			clusterMap.put(request.getGroupTag(), nMap);
		}

		printDirectory();
	}

	private static void printDirectory() {
		System.out.println("Printing Cluster Directory");
		for (Map.Entry<String, Map<String, Node>> entry : clusterMap.entrySet()) {
			for (Map.Entry<String, Node> entry2 : entry.getValue().entrySet()) {
				System.out.println("Group " + entry.getKey() + " NodeId " + entry2.getKey() + " IP "
						+ entry2.getValue().getNodeIp());
			}

		}
	}
	
	public Map<String, Node> getNodeMap(){
		
		return clusterMap.get(MyConstants.GROUP_NAME);
	}
	
	public Map<String, Map<String, Node>> getGroupMap(){
    	return clusterMap;
    }
}
