package com.sjsu.rollbits.datasync.server.resources;

public interface RollbitsConstants {
	String SUCCESS = "success";
	String FAILED = "failed";
	String INTERNAL = "INTERNAL";
	String INTER_CLUSTER = "INTER_CLUSTER";
	String CLIENT = "CLIENT";

	// error codes
	String CONNECTION_FAILED = "CONNECTION_FAILED";
	String REPLICATION_FACTOR = "replicationfactor";
	String VIRTUAL_NODES = "virtualnodes";

	//property names from property files
	String DB_URL = "url";
	String DB_USERNAME = "username";
	String DB_PASSWORD = "password";
	String CLUSTER_NAME = "ClusterName";
	String NODE_NAME = "NodeName";
	String NODE_PORT = "NodePort";
	String NODE_IP = "NodeIP";
	String SECRET = "Secret";
	String UDP_BROADCAST_IP = "UDP_IP_Broadcast";
	String UDP_PORT = "UDP_Port";
	
	
}
