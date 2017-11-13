/**
 * 
 */
package com.sjsu.rollbits.raft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.MyConstants;
import com.sjsu.rollbits.discovery.Node;

import routing.Pipe.FailoverMessage;
import routing.Pipe.Route;
import routing.Pipe.Route.Path;

/**
 * @author nishantrathi
 *
 */
public class RaftHelper {
	
	/**
	 * 
	 */
	public RaftHelper() {
	}

	
	public static void broadcast(Route.Builder routeBuilder){
		Map<String,Node> nodeMap = ClusterDirectory.getNodeMap();
		List<Node> failedList = new ArrayList<>();
		for (Map.Entry<String, Node> entry : nodeMap.entrySet())
		{
			Node node = entry.getValue();
			MessageClient mc = new MessageClient(node.getNodeIp(), node.getPort());
			RaftListener rl = new RaftListener(node, mc, routeBuilder);
			mc.addListener(rl);
			if (!mc.sendProto(routeBuilder)){
				failedList.add(node);
			}
		}
		if(failedList.size()>0 && failedList.size() == ClusterDirectory.getNodeMap().size()){
			//I am the leader and I Go Down
			RaftContext.getInstance().setRaftState(new RaftFollowerState());
		} else {
			//handle failover
			broadcastFailover(failedList);
		}
		
	}
	
	public static String getMyNodeId(){
		return MyConstants.NODE_NAME;
	}


	public static Integer requiredMajorityCount() {
		// TODO Auto-generated method stub
		int requiredMajority = ClusterDirectory.getNodeMap().size()/2 -1;
		return requiredMajority<=0?0:requiredMajority;
	}


	public static void sendMessageToNode(String senderNodeId, Route.Builder routeBuild) {
		Node node = ClusterDirectory.getNodeMap().get(senderNodeId);
		MessageClient mc = new MessageClient(node.getNodeIp(), node.getPort());
		RaftListener rl = new RaftListener(node,mc,routeBuild);
		mc.addListener(rl);
		mc.sendProto(routeBuild);
	}

	public static void broadcastFailover(List<Node> nodes){
		for (Node node : nodes)
		{
			Route.Builder routeBuilder = Route.newBuilder();
			routeBuilder.setPath(Path.FAILOVER_MSG);
			FailoverMessage.Builder failoverMessageBuilder = FailoverMessage.newBuilder();
			failoverMessageBuilder.setNodeName(node.getNodeId());
			routeBuilder.setFailoverMessage(failoverMessageBuilder);
			broadcast(routeBuilder);
		}
	}
	
}
