package com.sjsu.rollbits.sharding.hashing;

public class RNode {
	private String nodeId;
	private Type type;
	private String ipAddress;
	private int port;
	
	public enum Type {
		REPLICA, PRIMARY;

	}

	public RNode() {
		// TODO Auto-generated constructor stub
	}

	public RNode(String nodeId, Type type, String ipAddress, int port) {
		this.nodeId = nodeId;
		this.type = type;
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public RNode(String nodeId, String ipAddress) {
		this.nodeId = nodeId;
		this.ipAddress = ipAddress;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	

}


