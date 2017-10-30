/**
 * 
 */
package com.sjsu.rollbits.discovery;

import java.util.HashMap;
import java.util.Map;

import routing.Pipe;
import routing.Pipe.NetworkDiscoveryPacket;

/**
 * @author nishantrathi
 *
 */
public class NetworkInMemoryDB {
	
	private static Map<String, Node> mp = new HashMap<>();
	private static Map<String, Map<String, Node>> mpMaps = new HashMap<String, Map<String, Node>>();

	/**
	 * 
	 */
	private NetworkInMemoryDB() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void addNode(NetworkDiscoveryPacket packet){
//		Node node = new Node(packet.getNodeid(), "", packet.getGroup());
//		mp.put(packet.getNodeid(), node);
//		if(mpMaps.containsKey(packet.getGroup())){
//			mpMaps.get(packet.getGroup()).put(packet.getNodeid(),node);
//		} else {
//			Map<String,Node> map = new HashMap<String,Node>();
//			map.put(packet.getNodeid(),node);
//			mpMaps.put(packet.getGroup(), map);
//		}
		
	}
	
	public static Map<String, Map<String, Node>> getAllNodes(){
		return mpMaps;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
