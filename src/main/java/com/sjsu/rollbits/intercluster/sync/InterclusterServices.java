/**
 * 
 */
package com.sjsu.rollbits.intercluster.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.Node;

import routing.Pipe.Message;
import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class InterclusterServices {
	
	Random rand = new Random();
	
	class CheckUserTask implements Callable<Boolean>, CommListener {
		
		MessageClient mc;
		String username;
		
		/**
		 * @param username
		 */
		public CheckUserTask(String host, int port, String username) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.username = username;
		}



		@Override
		public Boolean call() throws Exception {
			return mc.checkUserExists(username);
		}



		@Override
		public String getListenerID() {
			return "checkUserTask";
		}



		@Override
		public void onMessage(Route msg) {
			System.out.println("CheckUserTask\n"+msg);
			
		}
		
	}
	
class FetchMessageTask implements Callable<List<Message>>, CommListener {
		
		MessageClient mc;
		String username;
		
		/**
		 * @param username
		 */
		public FetchMessageTask(String host, int port, String username) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.username = username;
		}



		@Override
		public List<Message> call() throws Exception {
			return mc.fetchMessages(username);
		}



		@Override
		public String getListenerID() {
			return "fetchMessageTask";
		}



		@Override
		public void onMessage(Route msg) {
			System.out.println("FetchMessageTask\n"+msg);
			
		}
		
	}
	
	public List<Future<Boolean>> doesUserExists(String userName) {
		List<Future<Boolean>> resultList = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(ClusterDirectory.getGroupMap().size());
		Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
		for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
			Map<String, Node> nodeMap = entry.getValue();
			Node nodeOfEachGroup = null;
			List<Node> nodeList = new ArrayList<>(nodeMap.values());
			nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
			CheckUserTask task = new CheckUserTask(nodeOfEachGroup.getNodeIp(), nodeOfEachGroup.getPort(), userName);
			Future<Boolean> isUserFound = executorService.submit(task);
			resultList.add(isUserFound);
		}

		return resultList;
	}
	
	public List<Future<List<Message>>> fetchAllMessages(String userName) {
		List<Future<List<Message>>> resultList = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(ClusterDirectory.getGroupMap().size());
		Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
		for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
			Map<String, Node> nodeMap = entry.getValue();
			Node nodeOfEachGroup = null;
			List<Node> nodeList = new ArrayList<>(nodeMap.values());
			nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
			FetchMessageTask task = new FetchMessageTask(nodeOfEachGroup.getNodeIp(), nodeOfEachGroup.getPort(),
					userName);
			Future<List<Message>> messgeList = executorService.submit(task);
			resultList.add(messgeList);
		}

		return resultList;
	}

}
