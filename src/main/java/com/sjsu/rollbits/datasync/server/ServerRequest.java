/**
 * 
 */
package com.sjsu.rollbits.datasync.server;

import java.util.concurrent.Callable;

import com.sjsu.rollbits.datasync.server.resources.RouteResource;

import io.netty.channel.Channel;
import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class ServerRequest implements Callable<Route> {

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
	public Route call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
