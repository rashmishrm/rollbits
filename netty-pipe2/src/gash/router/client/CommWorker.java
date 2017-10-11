package gash.router.client;

import io.netty.channel.Channel;
import routing.Pipe.Route;

import gash.router.app.DemoApp;

/**
 * queues outgoing messages - this provides surge protection if the client
 * creates large numbers of messages.
 * 
 * @author gash
 * 
 */
public class CommWorker extends Thread {
	private CommConnection conn;
	private boolean forever = true;

	public CommWorker(CommConnection conn) {
		this.conn = conn;

		if (conn.outbound == null)
			throw new RuntimeException("connection worker detected null queue");
	}

	@Override
	public void run() {

		boolean tryc=false;
		System.out.println("--> starting worker thread");
		System.out.flush();
		Channel ch=null;
		try {
			 ch = conn.connect();

		}catch(Exception e){
tryc=true;
		}

		if (ch == null || !ch.isOpen() || !ch.isActive()) {
			CommConnection.logger.error("connection missing, no outbound communication");
			for(ServerDetail d: DemoApp.sv) {

				try {
					CommConnection.initConnection(d.host, d.port);
					ch=conn.connect();
					break;
				}catch(Exception e){
					System.out.println("Connection faield yto"+d.host);
					continue;
				}
			}


			return;
		}

		while (true) {
			if (!forever && conn.outbound.size() == 0)
				break;

			try {
				// block until a message is enqueued AND the outgoing
				// channel is active
				Route msg = conn.outbound.take();
				if (ch.isWritable()) {
					if (!conn.write(msg)) {
						conn.outbound.putFirst(msg);
					}

					System.out.flush();
				} else {
					System.out.println("--> channel not writable- tossing out msg!");

					for(ServerDetail d: DemoApp.sv) {

						try {
							CommConnection.initConnection(d.host, d.port);
							ch=conn.connect();

						System.out.println("sending message to "+d.host);
							if (ch.isWritable()) {
								if (!conn.write(msg)) {
									conn.outbound.putFirst(msg);
								}
							}


							break;
						}catch(Exception e){
							System.out.println("Connection faield yto    "+d.host);
							continue;
						}
					}

					System.out.flush();


					// conn.outbound.putFirst(msg);
				}

				System.out.flush();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				break;
			} catch (Exception e) {
				CommConnection.logger.error("Unexpected communcation failure", e);
				break;
			}
		}

		if (!forever) {
			CommConnection.logger.info("connection queue closing");
		}
	}
}
