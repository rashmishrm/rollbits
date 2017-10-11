/**
 * Copyright 2016 Gash.
 *
 * This file and intellectual content is protected under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package gash.router.server;

import java.beans.Beans;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gash.router.container.RoutingConf;
import gash.router.server.resources.RouteResource;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import routing.Pipe.Route;

/**
 * The message handler processes json messages that are delimited by a 'newline'
 * 
 * TODO replace println with logging!
 * 
 * @author gash
 * 
 */
public class ServerHandler extends SimpleChannelInboundHandler<Route> {
	protected static Logger logger = LoggerFactory.getLogger("connect");

	private HashMap<String, String> routing;

	public ServerHandler(RoutingConf conf) {
		if (conf != null)
			routing = conf.asHashMap();
	}

	/**
	 * override this method to provide processing behavior. This implementation
	 * mimics the routing we see in annotating classes to support a RESTful-like
	 * behavior (e.g., jax-rs).
	 * 
	 * @param msg
	 */
	public void handleMessage(Route msg, Channel channel) {
		if (msg == null) {
			// TODO add logging
			System.out.println("ERROR: Unexpected content - " + msg);
			return;
		}

		System.out.println("---> " + msg.getId() + ": " + msg.getPath() + ", " + msg.getPayload());

		try {
			String clazz = routing.get(msg.getPath().toLowerCase());
			if (clazz != null) {
				RouteResource rsc = (RouteResource) Beans.instantiate(RouteResource.class.getClassLoader(), clazz);
				try {
					String reply = rsc.process(msg.getPayload());
					System.out.println("---> reply: " + reply);
					if (reply != null) {
						Route.Builder rb = Route.newBuilder(msg);
						rb.setPayload(reply);
						channel.write(rb.build());
					}
				} catch (Exception e) {
					// TODO add logging
					Route.Builder rb = Route.newBuilder(msg);
					rb.setPayload("Error: " + e.getMessage());
					channel.write(rb.build());
				}
			} else {
				// TODO add logging
				System.out.println("ERROR: unknown path - " + msg.getPath());
			}
		} catch (Exception ex) {
			// TODO add logging
			System.out.println("ERROR: processing request - " + ex.getMessage());
		}

		System.out.flush();
	}

	/**
	 * a message was received from the server. Here we dispatch the message to
	 * the client's thread pool to minimize the time it takes to process other
	 * messages.
	 * 
	 * @param ctx
	 *            The channel the message was received from
	 * @param msg
	 *            The message
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Route msg) throws Exception {
		System.out.println("------------");
		handleMessage(msg, ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Unexpected exception from downstream.", cause);
		ctx.close();
	}

}