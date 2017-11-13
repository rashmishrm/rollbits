package com.sjsu.rollbits.client.serverdiscovery;
import java.net.InetAddress;
import java.net.UnknownHostException;

import routing.Pipe;

public class Node {

    private String nodeId;
    private String nodeIp;
    private int port;
    private String group;
    private Pipe.NetworkDiscoveryPacket.Sender typeNode;
   
	public Node(String id, String ip, String port, String group, Pipe.NetworkDiscoveryPacket.Sender Sender){
	  	this.nodeId = id;
	  	this.nodeIp = ip;
	  	this.group = group;
	  	this.typeNode = Sender;
	  	this.port = Integer.parseInt(port);
	} 
	
	public String getNodeIp(){
		return nodeIp;
		
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Pipe.NetworkDiscoveryPacket.Sender getTypeNode() {
		return typeNode;
	}

	public void setTypeNode(Pipe.NetworkDiscoveryPacket.Sender typeNode) {
		this.typeNode = typeNode;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	@Override
	public String toString() {
		return "Node [nodeId=" + nodeId + ", nodeIp=" + nodeIp + ", port=" + port + ", group=" + group + ", typeNode="
				+ typeNode + "]";
	}
	
	
	
	
}
