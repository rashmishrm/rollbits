/*
 * copyright 2016, gash
 * 
 * Gash licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sjsu.rollbits.datasync.client;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.sjsu.rollbits.exception.ConnectionNotFormedException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import routing.Pipe.Route;

/**
 * provides an abstraction of the communication to the remote server.
 * 
 * @author gash
 * 
 */
public class CommConnection {

	protected static Logger logger = Logger.getLogger("CommConnection");

	protected static AtomicReference<CommConnection> instance = new AtomicReference<CommConnection>();

	private String host;
	private int port;
	private ChannelFuture channel; // do not use directly call
	// connect()!

	private EventLoopGroup group;

	// our surge protection using a in-memory cache for messages
	LinkedBlockingDeque<Route> outbound;

	// message processing is delegated to a threading model
	private CommWorker worker;

	/**
	 * Create a connection instance to this host/port. On construction the
	 * connection is attempted.
	 * 
	 * @param host
	 * @param port
	 */
	protected CommConnection(String host, int port) throws ConnectionNotFormedException {
		this.host = host;
		this.port = port;

		init();
	}

	protected CommConnection(String host, int port, boolean check) {
		this.host = host;
		this.port = port;

		// init();
	}

	public static CommConnection initConnection(String host, int port) {
		// 3 retries for Connection creation
		int retries = 1;
		while (retries <= 3) {
			try {

				/***
				 * Have created constructor CommConnection(host, port, true), which just creates
				 * instance of CommConnection, doesnt actually creates aby connection.
				 * CommConnection class implements custom equals and hashcode, so that If there
				 * is already any istance with same host and port, new instance is not created,
				 * albeit same refernce is used.
				 */
				instance.compareAndSet(null, new CommConnection(host, port));

				/**
				 * Checks whether with same host and port another connection reference exists
				 */
				if (!instance.get().equals(new CommConnection(host, port, true))) {
					instance.set(new CommConnection(host, port));
				}
				System.out.println(instance.get());
				break;

			} catch (ConnectionNotFormedException e) {
				logger.error("Trying to connect again: Retrying  " + retries + " time");

			}

			retries++;
		}

		return retries == -1 ? null : instance.get();

	}

	public static CommConnection getInstance() {
		// TODO throw exception if not initialized!
		return instance.get();
	}

	/**
	 * release all resources
	 */
	public void release() {
		if (channel != null) {
			channel.cancel(true);
			if (channel.channel() != null)
				channel.channel().close();
		}
		group.shutdownGracefully();
	}

	public void enqueue(Route req) throws Exception {
		// enqueue message
		outbound.put(req);
	}

	/**
	 * messages pass through this method (no queueing). We use a blackbox design as
	 * much as possible to ensure we can replace the underlining communication
	 * without affecting behavior.
	 * 
	 * NOTE: Package level access scope
	 * 
	 * @param msg
	 * @return
	 */
	public boolean write(Route msg) {
		if (msg == null)
			return false;
		else if (channel == null)
			throw new RuntimeException("missing channel");

		// TODO a queue is needed to prevent overloading of the socket
		// connection. For the demonstration, we don't need it

		ChannelFuture cf = connect().writeAndFlush(msg);

		if (cf.isDone() && !cf.isSuccess()) {
			logger.error("failed to send message to server - " + msg.getId());
			return false;
		}

		return true;
	}

	/**
	 * abstraction of notification in the communication
	 * 
	 * @param listener
	 */
	public void addListener(CommListener listener) {
		CommHandler handler = connect().pipeline().get(CommHandler.class);
		if (handler != null)
			handler.addListener(listener);
	}

	private boolean init() throws ConnectionNotFormedException {
		System.out.println("--> initializing connection to " + host + ":" + port);
		boolean created = true;
		// the queue to support client-side surging
		outbound = new LinkedBlockingDeque<Route>();

		group = new NioEventLoopGroup();
		try {

			CommInit si = new CommInit(false);
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(si);
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.SO_KEEPALIVE, true);

			// Make the connection attempt.
			channel = b.connect(host, port).syncUninterruptibly();

			// want to monitor the connection to the server s.t. if we loose the
			// connection, we can try to re-establish it.
			ClientClosedListener ccl = new ClientClosedListener(this);
			channel.channel().closeFuture().addListener(ccl);

			System.out.println(channel.channel().localAddress() + " -> open: " + channel.channel().isOpen()
					+ ", write: " + channel.channel().isWritable() + ", reg: " + channel.channel().isRegistered());

			// start outbound message processor
			worker = new CommWorker(this);
			worker.setDaemon(true);
			worker.start();

		} catch (Throwable ex) {
			created = false;
			logger.error("failed to initialize the client connection" + ExceptionUtils.getMessage(ex));

			// release resources
			release();
			throw new ConnectionNotFormedException("failed to initialize the client connection");
		}

		return created;

	}

	/**
	 * create connection to remote server
	 * 
	 * @return
	 */
	protected Channel connect() {
		// Start the connection attempt.
		if (channel == null) {
			try {
				logger.info("Re-Attempting Connection!");
				init();

			} catch (ConnectionNotFormedException c) {
				throw new RuntimeException("Not able to establish connection to server");
			}
		}

		if (channel != null && channel.isSuccess() && channel.channel().isWritable())
			return channel.channel();
		else
			try {
				logger.info("Re-Attempting Connection!");
				init();
				return channel.channel();
			} catch (ConnectionNotFormedException c) {
				throw new RuntimeException("Not able to establish connection to server");
			}

	}

	/**
	 * usage:
	 * 
	 * <pre>
	 * channel.getCloseFuture().addListener(new ClientClosedListener(queue));
	 * </pre>
	 * 
	 * @author gash
	 * 
	 */
	public static class ClientClosedListener implements ChannelFutureListener {
		CommConnection cc;

		public ClientClosedListener(CommConnection cc) {
			this.cc = cc;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			// we lost the connection or have shutdown.
			System.out.println("--> client lost connection to the server");
			System.out.flush();

			// @TODO if lost, try to re-establish the connection
			logger.info("Reattempting creating connection with listener");
			this.cc = CommConnection.initConnection(cc.host, cc.port);

		}
	}

	@Override
	public boolean equals(Object obj) {
		CommConnection c = (CommConnection) obj;
		return this.host.equals(c.host);
	}

	@Override
	public int hashCode() {
		return host.hashCode();
	}
}
