package com.sjsu.rollbits.discovery;

import java.util.Random;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import routing.Pipe.NetworkDiscoveryPacket;
import routing.Pipe.NetworkDiscoveryPacket.Mode;
import routing.Pipe.NetworkDiscoveryPacket.Sender;

public class UdpServerHandler extends SimpleChannelInboundHandler<NetworkDiscoveryPacket> {

	private static final Random random = new Random();
	// private static Map<String, Node> mp = Collections.emptyMap();
	// private static Map<String, Map<String, Node>> mpMaps =
	// Collections.emptyMap();

	// public static Map<String, Node> NodeMap= new HashMap<>();

	/*public static Map<String, Map<String, Node>> GroupMap = new HashMap<String, Map<String, Node>>();*/

	@Override
	public void channelRead0(ChannelHandlerContext ctx, NetworkDiscoveryPacket request) throws Exception {
		
		//Dont do anything when youare yourself sending the broadcast
		if(MyConstants.NODE_IP.equals(request.getNodeAddress())){
			return;
		}
		
		if (request.getSender().equals(Sender.EXTERNAL_SERVER_NODE)) {
			ClusterDirectory.addToDirectory(request);
		}

		if (request.getMode() == NetworkDiscoveryPacket.Mode.REQUEST) {

			try {
				NetworkDiscoveryPacket.Builder toSend = NetworkDiscoveryPacket.newBuilder();
				toSend.setGroupTag(MyConstants.GROUP_NAME);

				toSend.setNodeId(MyConstants.NODE_NAME);
				toSend.setNodeAddress(MyConstants.NODE_IP);
				toSend.setMode(Mode.RESPONSE);
				toSend.setNodePort(MyConstants.NODE_PORT);
				toSend.setSender(Sender.EXTERNAL_SERVER_NODE);
				toSend.setSecret(MyConstants.SECRET);
				NetworkDiscoveryPacket myResponse = toSend.build();
				UdpClient.sendUDPMessage(myResponse, request.getNodeAddress(), MyConstants.UDP_PORT);
			} catch (Exception e) {
				System.err.println("Exception received");
				e.printStackTrace();
			}
		}
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
