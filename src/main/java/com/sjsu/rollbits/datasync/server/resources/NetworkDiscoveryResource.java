package com.sjsu.rollbits.datasync.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.UdpClient;
import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.Channel;
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
	public Object process(Pipe.Route route, Channel returnChannel) {

		System.out.println("Recieved a packet" + route);
		NetworkDiscoveryPacket request = route.getNetworkDiscoveryPacket();
		System.out.println("Recieved a packet from" + request.getNodeAddress());
		// Dont do anything when youare yourself sending the broadcast
		if (Loadyaml.getProperty("NodeIP").equals(request.getNodeAddress())) {
			return null;
		}

		Route.Builder rb = Route.newBuilder();
		rb.setPath(Route.Path.NETWORK_DISCOVERY);

		NetworkDiscoveryPacket.Builder toSend = NetworkDiscoveryPacket.newBuilder();
		toSend.setGroupTag(Loadyaml.getProperty("ClusterName"));

		toSend.setNodeId(Loadyaml.getProperty("NodeName"));
		toSend.setNodeAddress(Loadyaml.getProperty("NodeIP"));
		toSend.setMode(Mode.RESPONSE);
		toSend.setNodePort(Integer.parseInt(Loadyaml.getProperty("NodePort")));
		toSend.setSender(Sender.EXTERNAL_SERVER_NODE);
		toSend.setSecret(Loadyaml.getProperty("Secret"));

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
