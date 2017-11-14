package com.sjsu.rollbits.sharding.hashing;

public class RNode implements Comparable<RNode> {
	private String nodeId;
	private Type type;
	private String ipAddress;
	private int port;

	private int virtualNodeCount;

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

	public RNode(String nodeId, Type type, String ipAddress, int port, int virtualNodeCount) {
		this.nodeId = nodeId;
		this.type = type;
		this.ipAddress = ipAddress;
		this.port = port;
		this.virtualNodeCount = virtualNodeCount;
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

	public long getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNodeId();
	}

	public int getVirtualNodeCount() {
		return virtualNodeCount;
	}

	public void setVirtualNodeCount(int virtualNodeCount) {
		this.virtualNodeCount = virtualNodeCount;
	}

	@Override
	public int compareTo(RNode o) {
		// TODO Auto-generated method stub
		return virtualNodeCount - o.virtualNodeCount;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return nodeId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		RNode node = (RNode) obj;

		return this.equals(node);
	}

}
