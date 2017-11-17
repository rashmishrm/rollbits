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
import com.sjsu.rollbits.yml.Loadyaml;

import routing.Pipe.Message;
import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class InterClusterServices2 implements ResultCollectable<List<Message>> {
	
	Random rand = new Random();
	int noOfResultExpected = 0;
	List<Message> resultList = new ArrayList<>();

	class FetchMessageTask implements  CommListener {

		MessageClient mc;
		String username;
		ResultCollectable<List<Message>> resultCollectable;

		/**
		 * @param username
		 */
		public FetchMessageTask(String host, int port, String username, ResultCollectable<List<Message>> resultCollectable) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.username = username;
			this.resultCollectable = resultCollectable;
		}

		//@Override
		public void doTask() {
			mc.fetchMessages(username);
		}

		@Override
		public String getListenerID() {
			return "fetchMessageTask";
		}

		@Override
		public void onMessage(Route msg) {
			System.out.println("FetchMessageTask\n" + msg);
			resultCollectable.collectResult(msg.getMessagesResponse().getMessagesList());
		}

	}

	public InterClusterServices2(int i) {
		noOfResultExpected = i;
	}
	
	public void recievedAResult() {
		--noOfResultExpected;
	}

	public void fetchAllMessages(String userName) {
		Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
		for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
			if(Loadyaml.getProperty("ClusterName").equals(entry.getKey())){
				continue;
			}
			Map<String, Node> nodeMap = entry.getValue();
			Node nodeOfEachGroup = null;
			List<Node> nodeList = new ArrayList<>(nodeMap.values());
			nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
			FetchMessageTask task = new FetchMessageTask(nodeOfEachGroup.getNodeIp(), nodeOfEachGroup.getPort(),
					userName, this);
			task.doTask();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InterClusterServices2 icservice = new InterClusterServices2(ClusterDirectory.getGroupMap().size() -1);
		icservice.fetchAllMessages("nishant");
	}

	@Override
	public void collectResult(List<Message> t) {
		resultList.addAll(t);
		noOfResultExpected--;
		if(noOfResultExpected==0){
			fetchResult();
		}
	}

	@Override
	public List<Message> fetchResult() {
		System.out.println(resultList);
		return resultList;
	}



}
