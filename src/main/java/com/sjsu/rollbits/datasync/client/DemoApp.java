
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

import routing.Pipe;
import routing.Pipe.Route;

public class DemoApp implements CommListener {
	private MessageClient mc;

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
		System.out.println("---here1---> " + msg);
	}

	/**
	 * sample application (client) use of our messaging service
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "10.0.0.175";
		int port = 4567;
		long stime = System.currentTimeMillis();

		try {
			MessageClient mc = new MessageClient(host, port);
			if (mc.isConnected()) {

				// mc.addUser("Rashmi", "Rashmi", RollbitsConstants.CLIENT,
				// false);
				System.out.println("Sleeping before connecting");
				// Thread.sleep(10000);
				mc.sendMessage("rashmishrm", "nishantrathi", "tsdhvsdkcsdkvnvnksnvksndvskvs", "CLIENT", false,RollbitsConstants.GROUP);
				mc.addGroup("Group6",4, "CLIENT", false);
				mc.addUsertoGroup(4, "Group6", "dhrumil", "CLIENT", false);
				//mc.fetchMessages("nishantrathi",RollbitsConstants.CLIENT);

			}

			// Route.Builder msg = ProtoUtil.createMessageRequest(1,
			// "nishant",true);

			// Route r = mc.sendSyncronousMessage(msg);

			// mc.addGroup("group", 1, RollbitsConstants.CLIENT, false);

			long etime = System.currentTimeMillis();

			System.out.println(etime - stime);

			Thread.sleep(10000);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommConnection.getInstance().release();
		}
	}
}
