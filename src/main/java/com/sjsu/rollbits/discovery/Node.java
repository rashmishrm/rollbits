package com.sjsu.rollbits.discovery;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class Node {

    private String nodeId;
    private String nodeIp;
    private int port;
   
	public Node(String id){
	  	nodeId = id;
	} 
	
	public String getNodeIp(){
		try {
			nodeIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodeIp;
		
	}
	
	public int Getport(){
		port = UdpClient.PORT;
		return port;
	}
	
	
}
