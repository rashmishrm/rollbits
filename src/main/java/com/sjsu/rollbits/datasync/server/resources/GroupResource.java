package com.sjsu.rollbits.datasync.server.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.dao.interfaces.model.Group;
import com.sjsu.rollbits.dao.interfaces.service.GroupService;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import routing.Pipe;
import routing.Pipe.Route;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

public class GroupResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("group");
	private ShardingService shardingService;
	private GroupService dbService = null;

	public GroupResource() {
		shardingService = ShardingService.getInstance();
		dbService = new GroupService();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.GROUP;
	}

	@Override
	public Object process(Pipe.Route msg) {
		boolean success = false;
		Pipe.actionType option = msg.getUser().getAction();

		switch (option) {
		case GET:

			break;

		case PUT:
			Pipe.Group group = msg.getGroup();

			Pipe.Header header = msg.getHeader();

			if (header.getType() != null && !header.getType().equals("INTERNAL")) {

				System.out.println(group.getGname());

				List<RNode> nodes = shardingService.getNodes(new Message(group.getGname()));

				// save to database

				for (RNode node : nodes) {
					MessageClient mc = new MessageClient(node.getIpAddress(), node.getPort());
					if (node.getType().equals(RNode.Type.REPLICA)) {
						mc.addGroup(group.getGname(), (int) group.getGid(), true, true);
					} else {
						success = mc.addGroup(group.getGname(), (int) group.getGid(), true, false);

					}

				}
			}

			else {

				System.out.println("Adding to database!!!!!");
				Group dbgrp = new Group(group.getGname());
				dbService.persist(dbgrp);
				success = true;

			}
			break;
		}
		Route.Builder rb = Route.newBuilder();
		rb.setPayload(success ? "sucess" : "Failed");
		return rb;

	}
}
