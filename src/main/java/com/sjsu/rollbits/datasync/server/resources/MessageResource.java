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
package com.sjsu.rollbits.datasync.server.resources;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;
import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.Channel;
import routing.Pipe;
import routing.Pipe.Header;
import routing.Pipe.Route;
import routing.Pipe.Route.Path;

/**
 * processes requests of message passing - demonstration
 * 
 * @author gash
 * 
 */
public class MessageResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("message");
	private ShardingService shardingService;
	private MessageService dbService = null;

	public MessageResource() {
		shardingService = ShardingService.getInstance();
		dbService = new MessageService();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.MESSAGE;
	}

	@Override
	public Object process(Pipe.Route msg, Channel returnChannel) {
		boolean isSuccess = false;
		routing.Pipe.Message message = msg.getMessage();

		if (message.getType().equals(routing.Pipe.Message.Type.GROUP)) {

			// check whether node is primary and has to fetch from db.
			List<RNode> groupShards = shardingService.getNodes(new Message(message.getReceiverId()));

			if (groupShards != null
					&& Loadyaml.getProperty(RollbitsConstants.NODE_NAME).equals(groupShards.get(0).getNodeId())) {

			} else {

				Route.Builder rb = ProtoUtil.createGetGroupRequest(msg.getId(), message.getReceiverId(),
						RollbitsConstants.CLIENT);
			}

		}

		if (msg.getHeader() != null && msg.getHeader().getType() != null
				&& Header.Type.CLIENT.equals(msg.getHeader().getType())) {

			System.out.println("Message recieved from :" + message.getReceiverId());

			List<RNode> fromNames = shardingService.getNodes(new Message(message.getSenderId()));
			List<RNode> toNames = shardingService.getNodes(new Message(message.getReceiverId()));
			Set<RNode> set = new HashSet<RNode>();
			set.addAll(fromNames);
			set.addAll(toNames);

			// save to database

			for (RNode node : set) {
				MessageClient mc = new MessageClient(node.getIpAddress(), (int) node.getPort());
				if (node.getType().equals(RNode.Type.REPLICA)) {
					if (mc.isConnected())
						mc.sendMessage(message.getSenderId(), message.getReceiverId(), message.getPayload(),
								RollbitsConstants.INTERNAL, true, message.getType().toString());
				} else {
					if (mc.isConnected())
						isSuccess = mc.sendMessage(message.getSenderId(), message.getReceiverId(), message.getPayload(),
								RollbitsConstants.INTERNAL, false, message.getType().toString());

				}

			}

		} else {
			logger.info("Adding to Database");
			// User dbuser = new User(user.getUname(), user.getEmail());
			com.sjsu.rollbits.dao.interfaces.model.Message messageModel = new com.sjsu.rollbits.dao.interfaces.model.Message(
					1, new Date(), message.getSenderId(), message.getReceiverId(), message.getSenderId(),
					message.getPayload());
			dbService.persist(messageModel);
			isSuccess = true;
		}

		Route.Builder rb = ProtoUtil.createResponseRoute(msg.getId(), isSuccess, null,
				isSuccess ? RollbitsConstants.SUCCESS : RollbitsConstants.FAILED);

		return rb;
	}

}
