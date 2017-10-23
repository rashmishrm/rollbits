package com.sjsu.rollbits.discovery;																																																																																																																																																																																																																																																														

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//import DiscoveryThread.DiscoveryThreadHolder;

public class Server implements Runnable {
	DatagramSocket socket;
    Map<String, Node> mp = new HashMap<>();
	Map<String, Map<String, Node>> mpMaps = new HashMap<String, Map<String, Node>>();
	  @Override
	  public void run() {
	    try {
	      //Keep a socket open to listen to all the UDP trafic that is destined for this port
	      socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
	      socket.setBroadcast(true);

	      while (true) {
	        System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

	        //Receive a packet 
	       
	        byte[] recvBuf = new byte[15000];
	        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
	        socket.receive(packet);
            mp.put(packet.getAddress().getHostAddress(), new Node(packet.getAddress().getHostAddress()));
	        mpMaps.put(new String(packet.getData()), mp);
	       
	        //Packet received

	        for(String key: mpMaps.keySet()){
	        	System.out.println("Group ID: "+key);
	        }
	        
	        for(String key: mp.keySet()){
	        	System.out.println("Node ID: "+key);
	        }
	        
	        //See if the packet holds the right command (message)
	        String message = new String(packet.getData()).trim();

	        if (message.equals("1")) {
	          
	        	byte[] sendData = "Request from Node of GID 1".getBytes();

	          //Send a response
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
	          socket.send(sendPacket);

	          System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
	        }
	      }
	    } catch (IOException ex) {
	      Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
	    }
}

public static Server getInstance() {
  return ServerHolder.INSTANCE;
}

private static class ServerHolder {

  private static final Server INSTANCE = new Server();
}

public static void main(String[] args){
	  Server s = new Server();
	  s.run();
}
}
