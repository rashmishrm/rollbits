package com.sjsu.rollbits.discovery;



import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

//import akka.actor.ActorRef;


public class IncommingPacketHandler extends  SimpleChannelInboundHandler<DatagramPacket> {
	Map<InetAddress, Node> mp = new HashMap<>();
	Map<ByteBuf, Map<InetAddress, Node>> mpMaps = new HashMap<ByteBuf, Map<InetAddress, Node>>();
    IncommingPacketHandler(){

    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, DatagramPacket packet) throws Exception {
        final InetAddress srcAddr = packet.sender().getAddress();
        final ByteBuf buf = packet.content();
        final int rcvPktLength = buf.readableBytes();
        final byte[] rcvPktBuf = new byte[rcvPktLength];
        buf.readBytes(rcvPktBuf);
        //System.out.println("Inside incomming packet handler");
        mp.put(srcAddr, new Node(srcAddr));
        mpMaps.put(packet.content(), mp);
        
        for(ByteBuf key: mpMaps.keySet()){
        	System.out.println("Group ID: "+key);
        }
        
        for(InetAddress key: mp.keySet()){
        	System.out.println("Node ID: "+key);
        }

        //rcvPktProcessing(rcvPktBuf, rcvPktLength, srcAddr);
    }
}
