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
package com.sjsu.rollbits.datasync.server;

import java.beans.Beans;
import java.util.HashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.sjsu.rollbits.datasync.container.RoutingConf;
import com.sjsu.rollbits.datasync.server.resources.RouteResource;

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
	protected static Logger logger = Logger.getLogger("ServerHandler");
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

		logger.info("Got Message on server" + msg.getId() + ": " + msg.getPath() + ", ");
		try {
			String clazz = routing.get(msg.getPath().toString());
			if (clazz != null) {
				RouteResource rsc = (RouteResource) Beans.instantiate(RouteResource.class.getClassLoader(), clazz);
				try {
					ServerRequestQueue.getInstance().addRequestToQueue(new ServerRequest(rsc, channel, msg));
//					Route.Builder reply = (Route.Builder) rsc.process(msg, channel);
//					logger.info("Sending Reply to" + reply);
//					if (reply != null) {
//						// Route.Builder rb = Route.newBuilder(msg);
//						// rb.setPayload(reply);
//						channel.writeAndFlush(reply.build());
//					}
				} catch (Exception e) {
					// TODO add logging
					Route.Builder rb = Route.newBuilder(msg);

					e.printStackTrace();
					channel.write(rb.build());
				}
			} else {

				logger.error("Error: unknown path - " + msg.getPath());

			}
		} catch (Exception ex) {
			// TODO add logging
			logger.error("ERROR: processing request  " + ExceptionUtils.getMessage(ex));

		}

	}

	/**
	 * a message was received from the server. Here we dispatch the message to the
	 * client's thread pool to minimize the time it takes to process other messages.
	 * 
	 * @param ctx
	 *            The channel the message was received from
	 * @param msg
	 *            The message
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Route msg) throws Exception {
		System.out.println("------here23------" + msg);
		handleMessage(msg, ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Unexpected exception from downstream.", cause);
		ctx.close();
	}

}