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

import routing.Pipe.Route;
import java.util.List;

/**
 * front-end (proxy) to our service - functional-based
 * 
 * @author gash
 * 
 */
public class MessageClient {
	// track requests
	private long curID = 0;

	public MessageClient(List<ServerDetail> list) {
		init(list);
	}

	private void init(List<ServerDetail> list) {

	for(ServerDetail d: list) {

		try {
			CommConnection.initConnection(d.host, d.port);
			break;
		}catch(Exception e){
			System.out.println("Connection faield yto"+d.host);
			continue;
		}
	}



	}

	public void addListener(CommListener listener) {
		try {
			CommConnection.getInstance().addListener(listener);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ping() {
		// construct the message to send
		Route.Builder rb = Route.newBuilder();
		rb.setId(nextId());
		rb.setPath("/ping");
		rb.setPayload("ping");

		try {
			// direct no queue
			// CommConnection.getInstance().write(rb.build());

			// using queue
			CommConnection.getInstance().enqueue(rb.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postMessage(String msg) {
		// construct the message to send
		Route.Builder rb = Route.newBuilder();
		rb.setId(nextId());
		rb.setPath("/message");
		rb.setPayload(msg);

		try {
			CommConnection.getInstance().enqueue(rb.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void release() {
		CommConnection.getInstance().release();
	}

	/**
	 * Since the service/server is asychronous we need a unique ID to associate
	 * our requests with the server's reply
	 * 
	 * @return
	 */
	private synchronized long nextId() {
		return ++curID;
	}
}
