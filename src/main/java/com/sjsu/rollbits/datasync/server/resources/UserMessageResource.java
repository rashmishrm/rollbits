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
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.sjsu.rollbits.dao.interfaces.model.GroupUser;
import com.sjsu.rollbits.dao.interfaces.service.GroupUserService;
import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.intercluster.sync.InterClusterUserMessageService;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import io.netty.channel.Channel;
import routing.Pipe;
import routing.Pipe.Header;
import routing.Pipe.Route;

/**
 * processes requests of message passing - demonstration
 * 
 * @author gash
 * 
 */
public class UserMessageResource implements RouteResource {
	protected static Logger logger = Logger.getLogger("UserMessageResource");

	private ShardingService shardingService;
	private MessageService dbService = null;
	private GroupUserService gservice = null;

	public UserMessageResource() {
		shardingService = ShardingService.getInstance();
		dbService = new MessageService();
		gservice = new GroupUserService();
	}

	@Override
	public Pipe.Route.Path getPath() {
		return Pipe.Route.Path.MESSAGES_REQUEST;
	}

	@Override
	public Object process(Pipe.Route msg, Channel returnChannel) {
		// boolean isSuccess = false;
		//
		routing.Pipe.MessagesRequest message = msg.getMessagesRequest();
		//
		List<RNode> nodes = shardingService.getNodes(new Message(message.getId()));
		//
		Header header = null;
		if (msg.hasHeader()) {
			header = msg.getHeader();
		}

		RNode primaryNode = nodes.get(0);
		if (header != null && header.getType() != null && header.getType().equals(Header.Type.INTERNAL)) {
			List<com.sjsu.rollbits.dao.interfaces.model.Message> messages = null;
			List<GroupUser> groups = gservice.findGroupsForUser(message.getId());

			List<String> list = groups.stream().map(v -> v.getGroupid()).collect(Collectors.toList());
			
			if (list == null || list.size() == 0)
				messages = dbService.findAllforuname(message.getId());

			else
				messages = dbService.findAllMessages(message.getId(), list);
			Route.Builder rb = ProtoUtil.createMessageResponseRoute(msg.getId(), messages, message.getId(), true);

			return rb;
		} else if (header != null && header.getType() != null && header.getType().equals(Header.Type.INTER_CLUSTER)) {

			InterClusterUserMessageService ics = new InterClusterUserMessageService(primaryNode.getNodeId(),
					msg.getId(), returnChannel, message.getId(), true);
			ics.fetchAllMessages();

		} else {

			InterClusterUserMessageService ics = new InterClusterUserMessageService(primaryNode.getNodeId(),
					msg.getId(), returnChannel, message.getId(), false);
			ics.fetchAllMessages();

		}

		return null;
	}

}
