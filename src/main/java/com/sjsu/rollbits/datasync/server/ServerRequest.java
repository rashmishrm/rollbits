/**
 * 
 */
package com.sjsu.rollbits.datasync.server;

import java.util.concurrent.Callable;

import com.sjsu.rollbits.datasync.server.resources.RouteResource;

import io.netty.channel.Channel;
import routing.Pipe.Route;
import routing.Pipe.Route.Builder;

/**
 * @author nishantrathi
 *
 */
public class ServerRequest implements Callable<Route.Builder> {

	private Channel channel;
	private Route msg;
	private RouteResource resource;

	/**
	 * 
	 */
	public ServerRequest(RouteResource resource, Channel channel, Route msg) {
		this.channel = channel;
		this.msg = msg;
		this.resource = resource;
	}

	@Override
	public Route.Builder call() throws Exception {
		Route.Builder reply = (Builder) resource.process(msg, channel);
		if (reply != null) {
			channel.writeAndFlush(reply.build());
		}
		return reply;
	}

}
