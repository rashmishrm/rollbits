package com.sjsu.rollbits.datasync.server.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.raft.RaftContext;
import com.sjsu.rollbits.raft.RaftState;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import routing.Pipe;
import routing.Pipe.FailoverMessage;
import routing.Pipe.RaftMessage;
import routing.Pipe.Route;
import routing.Pipe.RaftMessage.RaftMsgType;
import routing.Pipe.Route.Path;

public class FailoverResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("raft");

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.RAFT_MSG;
	}

	@Override
	public Object process(Pipe.Route msg) {
		FailoverMessage failoverMessage = msg.getFailoverMessage();
		ClusterDirectory.handleFailover(failoverMessage.getNodeName());
		ShardingService.getInstance().reset();

		Route.Builder rb = Route.newBuilder();
		rb.setId(msg.getId());
		rb.setPath(Path.MSG);
		rb.setPayload("sucess");

		return rb;
	}

}
