/**
 * 
 */
package com.sjsu.rollbits.raft;

import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class RaftHelper {

	/**
	 * 
	 */
	public RaftHelper() {
		// TODO Auto-generated constructor stub
	}

	
	public static void broadcast(Route route){
		
	}
	
	public static String getMyNodeId(){
		return "MyLaptop";
	}


	public static Integer requiredMajorityCount() {
		// TODO Auto-generated method stub
		return 3;
	}


	public static void sendMessageToNode(String senderNodeId, Route build) {
		// TODO Auto-generated method stub
		
	}
	
	
}
