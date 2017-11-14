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

import routing.Pipe;
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
	public Object process(Pipe.Route msg) {
		boolean isSuccess = false;
		routing.Pipe.Message message = msg.getMessage();

		if (msg.getHeader() != null && msg.getHeader().getType() != null
				&& "EXTERNAL".equals(msg.getHeader().getType())) {

			System.out.println("Message recieved from :" + message.getFromuname());

			List<RNode> fromNames = shardingService.getNodes(new Message(message.getFromuname()));
			List<RNode> toNames = shardingService.getNodes(new Message(message.getTouname()));
			Set<RNode> set = new HashSet<RNode>();
			set.addAll(fromNames);
			set.addAll(toNames);

			// save to database

			for (RNode node : set) {
				MessageClient mc = new MessageClient(node.getIpAddress(), node.getPort());
				if (node.getType().equals(RNode.Type.REPLICA)) {
					mc.sendMessage(message.getFromuname(), message.getTouname(), message.getMessage(), true, true);
				} else {
					mc.sendMessage(message.getFromuname(), message.getTouname(), message.getMessage(), true, false);
				}

			}

		} else {
			System.out.println("Adding to database!!!!!");
			// User dbuser = new User(user.getUname(), user.getEmail());
			com.sjsu.rollbits.dao.interfaces.model.Message messageModel = new com.sjsu.rollbits.dao.interfaces.model.Message(
					1, new Date(), message.getFromuname(), message.getTouname(), message.getTogname(),
					message.getMessage());
			dbService.persist(messageModel);
			isSuccess = true;
		}

		Route.Builder rb = Route.newBuilder();
		rb.setId(msg.getId());
		rb.setPath(Path.MSG);
		rb.setPayload(isSuccess ? "sucess" : "Failed");

		return rb;
	}

}
