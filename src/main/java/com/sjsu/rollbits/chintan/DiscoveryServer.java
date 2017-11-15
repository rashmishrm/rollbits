package com.sjsu.rollbits.chintan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.datasync.container.RoutingConf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class DiscoveryServer implements Runnable {

    protected static Logger logger = LoggerFactory.getLogger("discovery");

    private RoutingConf conf;

    public DiscoveryServer(RoutingConf conf) {
        this.conf = conf;
    }

    @Override
    public void run() {
        logger.info("Discovery starting");

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new DiscoveryInit(conf, false));

            logger.info("Starting server, listening on port = " + 8888);
            ChannelFuture f = b.bind(8888).sync();


            logger.info(f.channel().localAddress() + " -> open: " + f.channel().isOpen() + ", write: "
                    + f.channel().isWritable() + ", act: " + f.channel().isActive());

            f.channel().closeFuture().await();

        } catch (InterruptedException ex) {
            // on bind().sync()
            logger.error("Failed to setup handler.", ex);
        } finally {
            group.shutdownGracefully();
        }
    }
}
