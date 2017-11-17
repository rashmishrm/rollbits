package com.sjsu.rollbits.datasync.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import io.netty.channel.Channel;
import routing.Pipe;
import routing.Pipe.FailoverMessage;
import routing.Pipe.Route;

public class FailoverResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("raft");

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.RAFT_MSG;
	}

	@Override
	public Object process(Pipe.Route msg, Channel returnChannel) {
		FailoverMessage failoverMessage = msg.getFailoverMessage();
		ClusterDirectory.handleFailover(failoverMessage.getNodeName());
		ShardingService.getInstance().reset();

		Route.Builder rb = ProtoUtil.createResponseRoute(msg.getId(), true, null, RollbitsConstants.SUCCESS);

		return rb;
	}

}
