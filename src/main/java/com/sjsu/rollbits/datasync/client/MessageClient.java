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
package com.sjsu.rollbits.datasync.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.sjsu.rollbits.datasync.server.resources.ProtoUtil;

import routing.Pipe;
import routing.Pipe.Message;
import routing.Pipe.Route;

/**
 * front-end (proxy) to our service - functional-based
 * 
 * @author gash
 * 
 */
public class MessageClient {
	// track requests
	private long curID = 0;

	ConcurrentHashMap<Long, Route> requestResponseMap = new ConcurrentHashMap<>();

	public MessageClient(String host, int port) {
		init(host, port);
	}

	private void init(String host, int port) {
		CommConnection.initConnection(host, port);
	}

	public void addListener(CommListener listener) {
		CommConnection.getInstance().addListener(listener);
	}

	public Route sendSyncronousMessage(Route.Builder msg) {
		// construct the message to send

		Route response = null;
		long id = nextId();
		msg.setId(id);

		try {

			this.addListener(new CommListener() {

				@Override
				public void onMessage(Route msg) {
					System.out.println("Recieved Message");
					requestResponseMap.put(msg.getId(), msg);
				}

				@Override
				public String getListenerID() {
					// TODO Auto-generated method stub
					return "userresource";
				}
			});

			CommConnection.getInstance().write(msg.build());

			while (true) {

				Thread.sleep(20000);
				System.out.println("Checking..... whether we recieved response!!!! for requestId" + id);
				if (requestResponseMap.get(id) != null) {
					response = requestResponseMap.get(id);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	// Send already formed message
	public void postOnQueue(Route.Builder msg) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean addUser(String name, String email, String type, boolean async) {
		boolean added = false;
		Route.Builder rb = ProtoUtil.createAddUserRequest(nextId(), name, type);
		CommConnection conn = CommConnection.getInstance();
		try {
			if (async)
				conn.enqueue(rb.build());
			else
				added = conn.write(rb.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return added;
	}

	public boolean addGroup(String name, int gid, String type, boolean async) {
		Route.Builder rb = ProtoUtil.createAddGroupRequest(gid, name, type);
		CommConnection conn = CommConnection.getInstance();
		boolean added = false;
		try {
			if (async)
				conn.enqueue(rb.build());
			else
				added = conn.write(rb.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return added;

	}

	public boolean sendMessage(String fromUserId, String toUserId, String message, String type, boolean async) {
		// construct the message to send
		boolean added = false;

		Route.Builder rb = ProtoUtil.addMessageRequest(nextId(), fromUserId, toUserId, message, type);
		CommConnection conn = CommConnection.getInstance();
		try {
			if (async)
				conn.enqueue(rb.build());
			else
				added = conn.write(rb.build());
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// conn.release();
		}
		return added;

	}

	public void release() {
		CommConnection.getInstance().release();
	}

	/**
	 * Since the service/server is asychronous we need a unique ID to associate our
	 * requests with the server's reply
	 * 
	 * @return
	 */
	private synchronized long nextId() {
		return ++curID;
	}

	public boolean sendProto(Route.Builder routeBuilder) {
		CommConnection conn = CommConnection.getInstance();
		routeBuilder.setId(nextId());
		return conn.write(routeBuilder.build());
	}

	public Boolean checkUserExists(String username) {
		// TODO send message to check if a username exists
		return false;
	}

	public List<Message> fetchMessages(String username, boolean user) {
		List<Message> messages = new ArrayList<Message>();

		Route.Builder msg = ProtoUtil.createMessageRequest(nextId(), username, user);

		Route r = sendSyncronousMessage(msg);

		if (r != null) {

			messages = r.getMessagesResponse().getMessagesList();
		}

		return messages;
	}
}
