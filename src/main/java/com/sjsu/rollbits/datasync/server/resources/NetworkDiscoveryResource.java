package com.sjsu.rollbits.datasync.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.MyConstants;
import com.sjsu.rollbits.discovery.UdpClient;

import routing.Pipe;
import routing.Pipe.NetworkDiscoveryPacket;
import routing.Pipe.Route;
import routing.Pipe.NetworkDiscoveryPacket.Mode;
import routing.Pipe.NetworkDiscoveryPacket.Sender;
import routing.Pipe.Route.Path;

public class NetworkDiscoveryResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("user");

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.NETWORK_DISCOVERY;
	}

	@Override
	public Object process(Pipe.Route route) {

		System.out.println("Recieved a packet" + route);
		NetworkDiscoveryPacket request = route.getNetworkDiscoveryPacket();
		System.out.println("Recieved a packet from" + request.getNodeAddress());
		// Dont do anything when youare yourself sending the broadcast
		if (MyConstants.NODE_IP.equals(request.getNodeAddress())) {
			return null;
		}

		Route.Builder rb = Route.newBuilder();
		rb.setPath(Route.Path.NETWORK_DISCOVERY);

		NetworkDiscoveryPacket.Builder toSend = NetworkDiscoveryPacket.newBuilder();
		toSend.setGroupTag(MyConstants.GROUP_NAME);

		toSend.setNodeId(MyConstants.NODE_NAME);
		toSend.setNodeAddress(MyConstants.NODE_IP);
		toSend.setMode(Mode.RESPONSE);
		toSend.setNodePort(Integer.parseInt(MyConstants.NODE_PORT));
		toSend.setSender(Sender.EXTERNAL_SERVER_NODE);
		toSend.setSecret(MyConstants.SECRET);

		rb.setNetworkDiscoveryPacket(toSend);
		rb.setId(1);

		if (request.getSender().equals(Sender.EXTERNAL_SERVER_NODE)) {
			ClusterDirectory.addToDirectory(request);
		}

		if (request.getMode() == NetworkDiscoveryPacket.Mode.REQUEST) {

			try {

				// UdpClient.sendUDPMessage(rb.build(), request.getNodeAddress(),
				// MyConstants.UDP_PORT);
			} catch (Exception e) {
				System.err.println("Exception received");
				e.printStackTrace();
			}
		}
		return rb;
	}

}
