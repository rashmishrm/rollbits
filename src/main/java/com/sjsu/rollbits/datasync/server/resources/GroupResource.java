package com.sjsu.rollbits.datasync.server.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.dao.interfaces.model.Group;
import com.sjsu.rollbits.dao.interfaces.model.GroupUser;
import com.sjsu.rollbits.dao.interfaces.service.GroupService;
import com.sjsu.rollbits.dao.interfaces.service.GroupUserService;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.intercluster.sync.InterClusterGroupUserService;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;
import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.Channel;
import routing.Pipe;
import routing.Pipe.Route;

public class GroupResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("group");
	private ShardingService shardingService;
	private GroupService dbService = null;
	private GroupUserService dbgroupuserService = null;

	public GroupResource() {
		shardingService = ShardingService.getInstance();
		dbService = new GroupService();
		dbgroupuserService = new GroupUserService();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.GROUP;
	}

	@Override
	public Object process(Pipe.Route msg, Channel returnChannel) {
		boolean success = false;
		Pipe.Group.ActionType option = msg.getGroup().getAction();

		switch (option) {
		case CREATE:
			Pipe.Group group = msg.getGroup();
			Pipe.Header header = msg.getHeader();
			// System.out.println(header);

			if (header != null && header.getType() != null && !header.getType().equals(Pipe.Header.Type.INTERNAL)) {

				System.out.println(group.getGname());

				List<RNode> nodes = shardingService.getNodes(new Message(group.getGname()));

				// save to database

				for (RNode node : nodes) {
					MessageClient mc = new MessageClient(node.getIpAddress(), (int) node.getPort());
					if (node.getType().equals(RNode.Type.REPLICA)) {
						success = mc.addGroup(group.getGname(), (int) group.getGid(), RollbitsConstants.INTERNAL, true);
					} else {
						success = mc.addGroup(group.getGname(), (int) group.getGid(), RollbitsConstants.INTERNAL,
								false);

					}

				}
			}

			else {

				Group dbgrp = new Group(group.getGname());
				dbService.persist(dbgrp);
				success = true;

			}
			success=true;
			Route.Builder rb = ProtoUtil.createResponseRoute(msg.getId(), success, null,
					success ? RollbitsConstants.SUCCESS : RollbitsConstants.FAILED);

			return rb;

		case ADDUSER:

			Pipe.Group groups = msg.getGroup();

			Pipe.Header headers = msg.getHeader();

			if (headers != null && headers.getType() != null && !headers.getType().equals(Pipe.Header.Type.INTERNAL)) {

				boolean sendTointercluster = headers.getType().equals(Pipe.Header.Type.INTER_CLUSTER) ? false : true;
				// checking if group doesn't exists with us, then send message to others.

				List<RNode> groupShards = shardingService.getNodes(new Message(groups.getGname()));

				if (groupShards != null
						&& Loadyaml.getProperty(RollbitsConstants.NODE_NAME).equals(groupShards.get(0).getNodeId())
						&& dbService.findIfAGroupExists(groups.getGname())) {

					GroupUser dbgrpuser = new GroupUser(groups.getGname(), groups.getUsername());
					dbgroupuserService.persist(dbgrpuser);
					success = true;
					rb = ProtoUtil.createResponseRoute(msg.getId(), success, null,
							success ? RollbitsConstants.SUCCESS : RollbitsConstants.FAILED);

					return rb;

					// check whether node is primary and has to fetch from db and no need to get
					// anything from other clusters.

				} else {

					InterClusterGroupUserService igs = new InterClusterGroupUserService(msg.getId(), returnChannel,
							groups.getGname(), groups.getGid(), groupShards, groups.getUsername(), sendTointercluster);
					igs.addUserToGroup();

					return null;
				}

			}

			else {

				GroupUser dbgrpuser = new GroupUser(groups.getGname(), groups.getUsername());
				dbgroupuserService.persist(dbgrpuser);
				success = true;

			}
			break;

		}

		return null;

	}
}
