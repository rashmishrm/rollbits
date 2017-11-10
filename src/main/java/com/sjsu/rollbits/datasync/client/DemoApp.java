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

	private void ping(int N) {
		// test round-trip overhead (note overhead for initial connection)
		final int maxN = 10;
		long[] dt = new long[N];
		long st = System.currentTimeMillis(), ft = 0;
		for (int n = 0; n < N; n++) {
			mc.ping();
			ft = System.currentTimeMillis();
			dt[n] = ft - st;
			st = ft;
		}

		System.out.println("Round-trip ping times (msec)");
		for (int n = 0; n < N; n++)
			System.out.print(dt[n] + " ");
		System.out.println("");

		// send a message
		st = System.currentTimeMillis();
		ft = 0;
		for (int n = 0; n < N; n++) {
			mc.postMessage("hello world " + n);
			ft = System.currentTimeMillis();
			dt[n] = ft - st;
			st = ft;
		}

		System.out.println("Round-trip post times (msec)");
		for (int n = 0; n < N; n++)
			System.out.print(dt[n] + " ");
		System.out.println("");
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
		String host = "10.0.0.1";
		int port = 4567;
		System.out.println(Pipe.Route.Path.USER);
		long stime= System.currentTimeMillis();

		try {
			MessageClient mc = new MessageClient(host, port);
			// DemoApp da = new DemoApp(mc);
			
			
			//mc.addUser("seconduser", "abc", false, true);
			//mc.addUser("nishantrathi", "rashmishrm74@gmail.com", false, true);
			
			
			
			mc.sendMessage("yahoooooo", "seconduser", 1, "Sending MEssage", false, true);
		

			long etime= System.currentTimeMillis();
			
			System.out.println(etime-stime);

			Thread.sleep(10);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommConnection.getInstance().release();
		}
	}
}
