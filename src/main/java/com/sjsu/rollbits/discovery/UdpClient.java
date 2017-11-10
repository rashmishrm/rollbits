package com.sjsu.rollbits.discovery;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;
import routing.Pipe.NetworkDiscoveryPacket;
import routing.Pipe.NetworkDiscoveryPacket.Sender;

public final class UdpClient {

	/*
	 * static final int PORT = Integer.parseInt(System.getProperty("port",
	 * "8080"));
	 */

	public static void main(String[] args) throws Exception {

		NetworkDiscoveryPacket.Builder builder = NetworkDiscoveryPacket.newBuilder();

		builder.setGroupTag(MyConstants.GROUP_NAME);
		// builder.setSender(NetworkDiscoveryPacket.Sender.END_USER_CLIENT);
		builder.setSender(NetworkDiscoveryPacket.Sender.EXTERNAL_SERVER_NODE);
		builder.setMode(NetworkDiscoveryPacket.Mode.REQUEST);
		builder.setNodeId(MyConstants.NODE_NAME);
		builder.setNodeAddress(MyConstants.NODE_IP);
		builder.setNodePort(MyConstants.NODE_PORT);

		// builder.setIp(InetAddress.getLocalHost().getHostAddress());
		// System.out.println("******"+InetAddress.getLocalHost().getHostAddress());

		NetworkDiscoveryPacket request = builder.build();// build() builds the
															// stream,
															// transitioning
															// this builder to
															// the built state.
		String IP = "255.255.255.255";
		int port = 8080;

		sendUDPMessage(request, IP, port);
	}

	public static void sendUDPMessage(NetworkDiscoveryPacket request, String IP, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new ChannelInitializer<DatagramChannel>() {
						@Override
						public void initChannel(DatagramChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new UdpClientHandler());
						}
					});

			Channel ch = b.bind(0).sync().channel();

			ByteBuf buf = Unpooled.copiedBuffer(request.toByteArray());

			ch.writeAndFlush(new DatagramPacket(buf, SocketUtils.socketAddress(IP, port))).sync();

			// UdpClientHandler will close the DatagramChannel when a
			// response is received. If the channel is not closed within 5
			// seconds,
			// print an error message and quit.
			if (!ch.closeFuture().await(5000)) {
				System.err.println("request timed out.");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
