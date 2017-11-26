/**
 * 
 */
package com.sjsu.rollbits.datasync.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nishantrathi
 *
 */
public class ServerRequestQueue implements Runnable {
	
	Integer NO_OF_THREADS = 50;
	
	Long TIME_BREAK = 1 * 50L;
	
	Queue<ServerRequest> serverRequestQueue = new ConcurrentLinkedQueue<>();
	
	ExecutorService executorService = Executors.newFixedThreadPool(NO_OF_THREADS);

	private static AtomicReference<ServerRequestQueue> serviceRequestQueue = new AtomicReference<ServerRequestQueue>();
	
	public void addRequestToQueue(ServerRequest request){
		serverRequestQueue.add(request);
	}
	
	private void addToExecutorService(){
		for(ServerRequest req: serverRequestQueue){
			executorService.submit(req);
		}
	}
	
	/**
	 * 
	 */
	private ServerRequestQueue() {
	}
	
	public static ServerRequestQueue getInstance(){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
