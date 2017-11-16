package com.sjsu.rollbits.discovery;

import java.util.HashMap;
import java.util.Map;

import com.sjsu.rollbits.yml.Loadyaml;

import routing.Pipe.NetworkDiscoveryPacket;

public class ClusterDirectory {


	
	
	public static Map<String, Map<String, Node>> clusterMap = new HashMap<String, Map<String, Node>>();

	public static synchronized void addToDirectory(NetworkDiscoveryPacket request) {
		Node node = new Node(request.getNodeId(), request.getNodeAddress(), request.getNodePort()+"",
				request.getGroupTag(), request.getSender());
		
		if (clusterMap.containsKey(request.getGroupTag())) {
			Map<String, Node> nMap = clusterMap.get(request.getGroupTag());
			nMap.put(request.getNodeId(), node);
		} else {
			Map<String, Node> nMap = new HashMap<>();
			nMap.put(request.getNodeId(), node);
			clusterMap.put(request.getGroupTag(), nMap);
		}
		
		//conf.loadyaml();
		printDirectory();
	}

	private static void printDirectory() {
		System.out.println("Printing Cluster Directory");
		System.out.println(clusterMap);
		/*for (Map.Entry<String, Map<String, Node>> entry : clusterMap.entrySet()) {
			for (Map.Entry<String, Node> entry2 : entry.getValue().entrySet()) {
				System.out.println("Group " + entry.getKey() + " NodeId " + entry2.getKey() + " IP "
						+ entry2.getValue().getNodeIp());
			}

		}*/
	}
	
	public static Map<String, Node> getNodeMap(){
		
		return clusterMap.get(Loadyaml.getProperty("ClusterName"));
	}
	
	public static Map<String, Map<String, Node>> getGroupMap(){
    	return clusterMap;
    }

	public static void handleFailover(String nodeName) {
		ClusterDirectory.getNodeMap().remove(nodeName);
		
	}
}
