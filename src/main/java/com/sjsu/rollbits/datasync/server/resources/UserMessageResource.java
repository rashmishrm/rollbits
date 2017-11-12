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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Extension.MessageType;
import com.sjsu.rollbits.Constants;
import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.sharding.hashing.Message;
import com.sjsu.rollbits.sharding.hashing.RNode;
import com.sjsu.rollbits.sharding.hashing.ShardingService;

import routing.Pipe;
import routing.Pipe.Route;
import routing.Pipe.actionType;

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
		return Pipe.Route.Path.USER_MESSAGES_REQUEST;
	}

	@Override
	public Object process(Pipe.Route msg) {
		boolean isSuccess = false;
		routing.Pipe.UserMessagesRequest message = msg.getUserMessagesRequest();

		List<RNode> nodes = shardingService.getNodes(new Message(message.getUname()));

		RNode primaryNode = nodes.get(0);
		Route.Builder rb = null;
		if (!primaryNode.getIpAddress().equals(Constants.MY_IP)) {
			MessageClient msgClient = new MessageClient(primaryNode.getIpAddress(), primaryNode.getPort());
			Route r = msgClient.sendSyncronousMessage(msg.toBuilder());
			rb = r.toBuilder();

		} else {

			List<com.sjsu.rollbits.dao.interfaces.model.Message> messages = dbService.findAll();

			System.out.println("hererrerererrerer in message resource");
			rb = Route.newBuilder();
			rb.setId(msg.getId());
			rb.setPath(Route.Path.USER_MESSAGES_RESPONSE);

			Pipe.UserMessagesResponse.Builder ub = Pipe.UserMessagesResponse.newBuilder();

			int i = 0;

			List<Pipe.Message> list = new ArrayList<>();

			for (com.sjsu.rollbits.dao.interfaces.model.Message mesg : messages) {
				if (mesg != null) {
					Pipe.Message.Builder m = Pipe.Message.newBuilder();
					m.setFromuname(mesg.getMessage() == null ? "" : mesg.getMessage());
					m.setTouname(mesg.getTouserid() == null ? "" : mesg.getTouserid());
					m.setAction(actionType.GET);
					m.setMessage(mesg.getMessage() == null ? "" : mesg.getMessage());
				
					list.add(m.build());
				}
			}
			ub.addAllMessages(list);
			ub.setUname(message.getUname());
			rb.setUserMessagesResponse(ub);
			

		}
		return rb;
	}

}
