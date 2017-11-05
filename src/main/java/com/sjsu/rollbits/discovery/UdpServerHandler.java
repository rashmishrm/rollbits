package com.sjsu.rollbits.discovery;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.netty.util.internal.SocketUtils;
import routing.Pipe.NetworkDiscoveryPacket;
import routing.Pipe.ServerNodeDiscoveryResponse;

public class UdpServerHandler extends SimpleChannelInboundHandler<NetworkDiscoveryPacket> {
    
    private static final Random random = new Random();
    //private static Map<String, Node> mp = Collections.emptyMap();
    //private static Map<String, Map<String, Node>> mpMaps = Collections.emptyMap();
    
    private static Map<String, Node> NodeMap= new HashMap<>();
    
    private static Map<String, Map<String, Node>> GroupMap = new HashMap<String, Map<String, Node>>();
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, NetworkDiscoveryPacket request) throws Exception {
        System.err.println("In channel read");
        
        if(!NodeMap.containsKey(request.getNodeid())){
    			NodeMap.put(request.getNodeid(), new Node(request.getNodeid()));
    			GroupMap.put(request.getGroup(), NodeMap);
    			
    	} 
        
        for(String key: GroupMap.keySet()){
        	System.out.println("Group ID: "+key);
        }
        
        for(String key: NodeMap.keySet()){
        	System.out.println("Node ID: "+key);
        }
        
        
        
//
        System.err.println(ctx.channel().attr(UdpServer.attkey).get());
        String clientIpPort = ctx.channel().attr(UdpServer.attkey).get();
        String clientIp = clientIpPort.split(":")[0];
        String clientPort = clientIpPort.split(":")[1];

        try {
            ServerNodeDiscoveryResponse.Builder toSend = ServerNodeDiscoveryResponse.newBuilder();
            toSend.setGroup("Group 1");

            toSend.setNodeid("Akansha");
            toSend.setIp("10.0.0.3");

            ServerNodeDiscoveryResponse myResponse = toSend.build();
            ByteBuf buf = Unpooled.copiedBuffer(myResponse.toByteArray());
            ctx.writeAndFlush(new DatagramPacket(buf, SocketUtils.socketAddress(clientIp.substring(1, clientIp.length()), Integer.parseInt(clientPort)))).sync();
        } catch (Exception e) {
            System.err.println("Exception received");
            e.printStackTrace();
        }
    }

    public Map<String, Node> getNodeMap(){
    	return NodeMap;
    }
    
    public Map<String, Map<String, Node>> getGroupMap(){
    	return GroupMap;
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
