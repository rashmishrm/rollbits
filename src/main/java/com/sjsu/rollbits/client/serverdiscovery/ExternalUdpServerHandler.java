package com.sjsu.rollbits.client.serverdiscovery;

import java.util.Random;

import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import routing.Pipe.NetworkDiscoveryPacket;
import routing.Pipe.NetworkDiscoveryPacket.Mode;
import routing.Pipe.NetworkDiscoveryPacket.Sender;
import routing.Pipe.Route;

public class ExternalUdpServerHandler extends SimpleChannelInboundHandler<Route> {

	private static final Random random = new Random();
	// private static Map<String, Node> mp = Collections.emptyMap();
	// private static Map<String, Map<String, Node>> mpMaps =
	// Collections.emptyMap();

	// public static Map<String, Node> NodeMap= new HashMap<>();

	/*
	 * public static Map<String, Map<String, Node>> GroupMap = new HashMap<String,
	 * Map<String, Node>>();
	 */

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Route route) throws Exception {
		System.out.println("Recieved a packet" + route);
		NetworkDiscoveryPacket request = route.getNetworkDiscoveryPacket();
		System.out.println("Recieved a packet from" + request.getNodeAddress());
		// Dont do anything when youare yourself sending the broadcast
		if (Loadyaml.getProperty("NodeIP").equals(request.getNodeAddress())) {
			return;
		}

		if (request.getSender().equals(Sender.EXTERNAL_SERVER_NODE)) {
			ExternalClientClusterDirectory.addToDirectory(request);
		}

//		if (request.getMode() == NetworkDiscoveryPacket.Mode.REQUEST) {
//
//			try {
//
//				Route.Builder rb = Route.newBuilder();
//				rb.setPath(Route.Path.NETWORK_DISCOVERY);
//
//				NetworkDiscoveryPacket.Builder toSend = NetworkDiscoveryPacket.newBuilder();
//				toSend.setGroupTag(Loadyaml.getProperty("ClusterName"));
//
//				toSend.setNodeId(Loadyaml.getProperty("NodeName"));
//				toSend.setNodeAddress(Loadyaml.getProperty("NodeIP"));
//				toSend.setMode(Mode.RESPONSE);
//				toSend.setNodePort(Integer.parseInt(Loadyaml.getProperty("NodePort")));
//				toSend.setSender(Sender.EXTERNAL_SERVER_NODE);
//				toSend.setSecret(Loadyaml.getProperty("Secret"));
//
//				rb.setNetworkDiscoveryPacket(toSend);
//				rb.setId(1);
//				UdpClient.sendUDPMessage(rb.build(), request.getNodeAddress(), Integer.parseInt(Loadyaml.getProperty("UDP_Port")));
//			} catch (Exception e) {
//				System.err.println("Exception received");
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		// We don't close the channel because we can keep serving requests.
	}
}
