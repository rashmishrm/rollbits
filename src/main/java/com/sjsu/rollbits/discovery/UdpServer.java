package com.sjsu.rollbits.discovery;

import com.sjsu.rollbits.datasync.container.RoutingConf;
import com.sjsu.rollbits.datasync.server.MessageServer;
import com.sjsu.rollbits.datasync.server.ServerInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.Bootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * Discards any incoming data.
 */
public class UdpServer {

    protected static Logger logger = LoggerFactory.getLogger("server");

    protected static HashMap<Integer, ServerBootstrap> bootstrap = new HashMap<Integer, ServerBootstrap>();

    public static final String sPort = "port";
    public static final String sPoolSize = "pool.size";

    protected RoutingConf conf;
    protected boolean background = false;

    public UdpServer(RoutingConf conf) {
        this.conf = conf;
    }

    public void release() {
    }

    public void startServer() {
        UdpServer.StartCommunication comm = new UdpServer.StartCommunication(conf);
        logger.info("Communication starting");

        if (background) {
            Thread cthread = new Thread(comm);
            cthread.start();
        } else
            comm.run();
    }

    /**
     * static because we need to get a handle to the factory from the shutdown
     * resource
     */
    public static void shutdown() {
        logger.info("Server shutdown");
        System.exit(0);
    }

    /**
     * initialize the server with a configuration of it's resources
     *
     * @param cfg
     */
    public UdpServer(File cfg) {
        init(cfg);
    }

    private void init(File cfg) {
        if (!cfg.exists())
            throw new RuntimeException(cfg.getAbsolutePath() + " not found");
        // resource initialization - how message are processed
        BufferedInputStream br = null;
        try {
            byte[] raw = new byte[(int) cfg.length()];
            br = new BufferedInputStream(new FileInputStream(cfg));
            br.read(raw);
            conf = MessageServer.JsonUtil.decode(new String(raw), RoutingConf.class);
            if (!verifyConf(conf))
                throw new RuntimeException("verification of configuration failed");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean verifyConf(RoutingConf conf) {
        return (conf != null);
    }

    /**
     * initialize netty communication
     *
     * @param port
     *            The port to listen to
     */
    private static class StartCommunication implements Runnable {
        RoutingConf conf;

        public StartCommunication(RoutingConf conf) {
            this.conf = conf;
        }

        public void run() {
            // construct boss and worker threads (num threads = number of cores)


            try {

                final NioEventLoopGroup group = new NioEventLoopGroup();

                    final Bootstrap b = new Bootstrap();
                    b.group( group ).channel( NioDatagramChannel.class )
                            .option( ChannelOption.SO_BROADCAST, true )
                            .handler( new UdpServerInit( conf, false ) );


                    b.option( ChannelOption.SO_BROADCAST, true );
                    // b.option(ChannelOption.SO_KEEPALIVE, true);
                    // b.option(ChannelOption.MESSAGE_SIZE_ESTIMATOR);

                    boolean compressComm = false;

                    // Start the server.
                    logger.info( "Starting server, listening on port = " + conf.getPort() );
                    Integer pPort = conf.getPort();
                    InetAddress address = InetAddress.getLocalHost();
                    System.out.printf( "waiting for message %s %s", String.format( pPort.toString() ), String.format( address.toString() ) );
                    b.bind( address, conf.getPort() ).sync().channel().closeFuture().await();

                    // block until the server socket is closed.

                } catch (Exception ex) {
                    // on bind().sync()
                    logger.error( "Failed to setup handler.", ex );
                } finally {
                    // Shut down all event loops to terminate all threads.
                    //bossGroup.shutdownGracefully();
                    //group.shutdownGracefully();
                }
            }
        }

        /**
         * help with processing the configuration information
         *
         * @author gash
         */
        public static class JsonUtil {
            private static MessageServer.JsonUtil instance;

            public static void init(File cfg) {

            }

            public static MessageServer.JsonUtil getInstance() {
                if (instance == null)
                    throw new RuntimeException( "Server has not been initialized" );

                return instance;
            }

            public static String encode(Object data) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.writeValueAsString( data );
                } catch (Exception ex) {
                    return null;
                }
            }

            public static <T> T decode(String data, Class<T> theClass) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue( data.getBytes(), theClass );
                } catch (Exception ex) {
                    return null;
                }
            }
        }


    }
