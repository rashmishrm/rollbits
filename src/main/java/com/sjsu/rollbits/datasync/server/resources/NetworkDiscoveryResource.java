package com.sjsu.rollbits.datasync.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.Pipe;
import routing.Pipe.Route;
import routing.Pipe.Route.Path;

public class NetworkDiscoveryResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("user");

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.NETWORK_DISCOVERY;
	}

	@Override
	public Object process(Pipe.Route msg) {

		Route.Builder rb = Route.newBuilder();
		rb.setPath(Path.MSG);
		rb.setId(msg.getId());

		rb.setPayload("success");
		return rb;
	}

}
