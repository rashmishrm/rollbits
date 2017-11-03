package com.sjsu.rollbits.discovery;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.util.AttributeKey;
import routing.Pipe.NetworkDiscoveryPacket;

import java.util.List;

public final class UdpServer {

    private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    static AttributeKey<String> attkey = AttributeKey.valueOf("clientid");
    
    

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<DatagramChannel>(){
                        @Override
                        public void initChannel(DatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                             pipeline.addLast("protobufDecoder", new MyDatagramPacketDecoder(new ProtobufDecoder(NetworkDiscoveryPacket.getDefaultInstance())));
                             pipeline.addLast("protobufEncoder", new ProtobufEncoder());
                             pipeline.addLast("handler", new UdpServerHandler());

                        }});

            b.bind(PORT).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
}
