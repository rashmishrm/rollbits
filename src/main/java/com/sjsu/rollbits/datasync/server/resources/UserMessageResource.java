/**
 * Copyright 2016 Gash.
 *
 * This file and intellectual content is protected under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sjsu.rollbits.datasync.server.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.discovery.MyConstants;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import routing.Pipe;
import routing.Pipe.Route;

/**
 * processes requests of message passing - demonstration
 * 
 * @author gash
 * 
 */
public class UserMessageResource implements RouteResource {
	protected static Logger logger = LoggerFactory.getLogger("usermessage");
	private ShardingService shardingService;
	private MessageService dbService = null;

	public UserMessageResource() {
		shardingService = ShardingService.getInstance();
		dbService = new MessageService();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.MESSAGES_REQUEST;
	}

	@Override
	public Object process(Pipe.Route msg) {
		boolean isSuccess = false;

		routing.Pipe.MessagesRequest message = msg.getMessagesRequest();

		List<RNode> nodes = shardingService.getNodes(new Message(message.getId()));

		RNode primaryNode = nodes.get(0);
		Route.Builder rb = null;
		if (!primaryNode.getIpAddress().equals(MyConstants.NODE_IP)) {
			MessageClient msgClient = new MessageClient(primaryNode.getIpAddress(), (int) primaryNode.getPort());
			Route r = msgClient.sendSyncronousMessage(msg.toBuilder());
			rb = r.toBuilder();

		} else {

			List<com.sjsu.rollbits.dao.interfaces.model.Message> messages = dbService.findAllforuname(message.getId());

			rb = ProtoUtil.createMessageResponseRoute(msg.getId(), messages, message.getId());

		}

		return rb;
	}

}
