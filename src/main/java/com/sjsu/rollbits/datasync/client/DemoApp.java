
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

import com.sjsu.rollbits.datasync.server.resources.RollbitsConstants;

import routing.Pipe.Route;

public class DemoApp implements CommListener {
	private MessageClient mc;

	long startTime = 0;

	long endTime = 0;

	int count = 0;

	static int requests = 100000;

	public DemoApp(MessageClient mc) {
		init(mc);
	}

	private void init(MessageClient mc) {
		this.mc = mc;
		this.mc.addListener(this);
	}

	@Override
	public String getListenerID() {
		return "demo";
	}

	@Override
	public void onMessage(Route msg) {
		// System.out.println("---here1---> " + msg);
		count++;
		// System.out.println("here"+count);
		if (count % 1000 == 0)
			System.out.println("**************************#### count         " + count + " $$$$ "
					+ (System.currentTimeMillis() - startTime));
		if (count >= (requests * 5))
			System.out.println("**************************         " + (System.currentTimeMillis() - startTime));
		if (msg.hasMessagesResponse()) {
			System.out.println("MESSAGE SIZE" + msg.getMessagesResponse().getMessagesList().size());
		}
	}

	/**
	 * sample application (client) use of our messaging service
	 * 
	 * @param argsac
	 */
	public static void main(String[] args) {
		// String host = "10.0.0.2";
		String host = "127.0.0.1";
		int port = 4567;
		long stime = System.currentTimeMillis();

		try {
			MessageClient mc = new MessageClient(host, port);
			DemoApp dm = new DemoApp(mc);
			dm.startTime = System.currentTimeMillis();
			if (mc.isConnected()) {
				// mc.sendMessage("user1", "user1", "Message for user1", "CLIENT", true,
				// RollbitsConstants.SINGLE);

				for (int i = 0; i < requests; i++) {

					mc.sendMessage("user1", "user1", "Message for user1", "CLIENT", true, RollbitsConstants.SINGLE);
					mc.sendMessage("user1", "user2", "Message for user1", "CLIENT", true, RollbitsConstants.SINGLE);
					mc.sendMessage("user1", "user3", "Message for user1", "CLIENT", true, RollbitsConstants.SINGLE);

					mc.sendMessage("user1", "user4", "Message for user1", "CLIENT", true, RollbitsConstants.SINGLE);

					mc.sendMessage("user1", "user5", "Message for user1", "CLIENT", true, RollbitsConstants.SINGLE);

					 if (i % 50000 == 0) {
					 Thread.sleep(50);
					 }

				}

				// mc.addUser("user1", "user1", RollbitsConstants.CLIENT,false);
				// Thread.sleep(10000);
				// mc.addGroup("Rollbits-App21Nov2017",4, "CLIENT", false);
				// mc.sendMessage("abcdefnov20", "Rollbits-App21Nov2017", "Send message to group
				// is working!!! Yay!", "CLIENT", false, RollbitsConstants.GROUP);
				// Route.Builder msg = ProtoUtil.createMessageRequest(1,
				// "nishant",true);

				// Route r = mc.sendSyncronousMessage(msg);

				// mc.addGroup("group", 1, RollbitsConstants.CLIENT, false);

				// mc.fetchMessages("user1", RollbitsConstants.CLIENT);

				System.out.println("Sent ALL");
				long etime = System.currentTimeMillis();

				System.out.println(etime - stime);
			}

			Thread.sleep(100000000000000000L);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommConnection.getInstance().release();
		}
	}
}
