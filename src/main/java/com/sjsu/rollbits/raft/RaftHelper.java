/**
 * 
 */
package com.sjsu.rollbits.raft;

import java.util.HashMap;
import java.util.Map;

import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class RaftHelper {
	
	Map<String, String> nodeMap = new HashMap<>();

	/**
	 * 
	 */
	public RaftHelper() {
		nodeMap.put("rashmi", "10.0.0.1");
		nodeMap.put("nishant", "10.0.0.2");
		nodeMap.put("akansha", "10.0.0.3");
		nodeMap.put("dhrumil", "10.0.0.4");
	}

	
	public static void broadcast(Route route){
		
	}
	
	public static String getMyNodeId(){
		return "nishant";
	}


	public static Integer requiredMajorityCount() {
		// TODO Auto-generated method stub
		return 3;
	}


	public static void sendMessageToNode(String senderNodeId, Route build) {
		// TODO Auto-generated method stub
		
	}
	
	
}
