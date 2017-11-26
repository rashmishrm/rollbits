/**
 * 
 */
package com.sjsu.rollbits.datasync.server;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class ServerRequestQueue implements Runnable {

	Integer NO_OF_THREADS = 50;

	Long TIME_BREAK = 1 * 50L;

	Integer BURST_SIZE = 1000;

	LinkedBlockingDeque<ServerRequest> serverRequestQueue = new LinkedBlockingDeque<ServerRequest>();

	ExecutorService executorService = Executors.newFixedThreadPool(NO_OF_THREADS);

	private static AtomicReference<ServerRequestQueue> serviceRequestQueue = new AtomicReference<ServerRequestQueue>();

	public void addRequestToQueue(ServerRequest request) {
		serverRequestQueue.add(request);
	}

	private void addToExecutorService() {
		int i = 0;

		while (i < BURST_SIZE) {
			if (serverRequestQueue.size() > 0) {
				ServerRequest sr = serverRequestQueue.poll();
				executorService.submit(sr);
			}
			i++;
		}
	}

	/**
	 * 
	 */
	private ServerRequestQueue() {
	}

	public static ServerRequestQueue getInstance() {
		serviceRequestQueue.compareAndSet(null, new ServerRequestQueue());
		return serviceRequestQueue.get();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(TIME_BREAK);
				addToExecutorService();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
