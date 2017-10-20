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
package gash.router.app;

import com.sun.security.ntlm.Server;
import gash.router.client.CommConnection;
import gash.router.client.CommListener;
import gash.router.client.MessageClient;
import gash.router.client.ServerDetail;
import routing.Pipe.Route;
import java.util.List;


import java.util.ArrayList;

public class DemoApp implements CommListener {
	private MessageClient mc;



	public static List<ServerDetail> sv= new ArrayList<ServerDetail>();
	static {
		sv.add(new ServerDetail("10.0.0.3",4567));
		sv.add(new ServerDetail("10.0.0.6",4567));
	}

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
		long st = System.nanoTime(), ft = 0;
		for (int n = 0; n < N; n++) {
			mc.ping();
			ft = System.nanoTime();
			dt[n] = ft - st;
			st = ft;
		}

		System.out.println("Round-trip ping times (msec)");
//		for (int n = 0; n < N; n++)
//			System.out.print(dt[n] + " ");
//		System.out.println("");

		// send a message
		st = System.nanoTime();
		ft = 0;
		for (int n = 0; n < 50; n++) {
			mc.postMessage("hello sending message" + n);
			System.out.println("Sending message");
			try {
				System.out.println("Sleepingzzz");

				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			ft = System.nanoTime();
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
		System.out.println("---> " + msg);
	}

	/**
	 * sample application (client) use of our messaging service
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "10.0.0.3";
		int port = 4567;



		try {
			MessageClient mc = new MessageClient(sv);
			DemoApp da = new DemoApp(mc);

			// do stuff w/ the connection
			da.ping(4000);

			System.out.println("\n** exiting in 10 seconds. **");
			System.out.flush();
			Thread.sleep(10 * 1000000000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommConnection.getInstance().release();
		}
	}
}
