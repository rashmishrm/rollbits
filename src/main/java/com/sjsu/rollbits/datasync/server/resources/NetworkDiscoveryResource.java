package com.sjsu.rollbits.datasync.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routing.Pipe;

public class NetworkDiscoveryResource  implements RouteResource {
    protected static Logger logger = LoggerFactory.getLogger("user");

    @Override
    public Pipe.Route.Path getPath() {
        return Pipe.Route.Path.NETWORK_DISCOVERY;
    }

    @Override
    public String process(Pipe.Route msg) {
    	

        return "success";
    }

}

