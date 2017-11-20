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

import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.datasync.server.resources.ProtoUtil;
import com.sjsu.rollbits.datasync.server.resources.RollbitsConstants;
import com.sjsu.rollbits.discovery.ClusterDirectory;
import com.sjsu.rollbits.discovery.Node;
import com.sjsu.rollbits.yml.Loadyaml;

import io.netty.channel.Channel;
import routing.Pipe.Message;
import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class InterClusterUserMessageService implements ResultCollectable<List<Message>> {

	Random rand = new Random();
	int noOfResultExpected = 0;
	List<Message> resultList = new ArrayList<>();
	private String primaryHostname;
	private boolean needToSendPacketToPrimary;
	private long routeId;
	private MessageService dbService = null;
	private Channel replyChannel;
	private String userName;
	private boolean intercluster;
	private boolean isResultPublished = false;

	/**
	 * @return the resultList
	 */
	public List<Message> getResultList() {
		return resultList;
	}

	class FetchMessageTask implements CommListener {

		MessageClient mc;
		String username;
		ResultCollectable<List<Message>> resultCollectable;
		private String requestType;

		/**
		 * @param username
		 */
		public FetchMessageTask(String host, int port, String username,
				ResultCollectable<List<Message>> resultCollectable, String requestType) {
			this.mc = new MessageClient(host, port);
			mc.addListener(this);
			this.username = username;
			this.resultCollectable = resultCollectable;
			this.requestType = requestType;
		}

		// @Override
		public void doTask() {
			mc.fetchMessages(username, requestType);
		}

		@Override
		public String getListenerID() {
			return "fetchMessageTask";
		}

		@Override
		public void onMessage(Route msg) {
			System.out.println("FetchMessageTask\n" + msg);
			if (msg.hasMessagesResponse())
				resultCollectable.collectResult(msg.getMessagesResponse().getMessagesList());
			else
				noOfResultExpected--;
		}

	}

	public InterClusterUserMessageService(String primaryHostname, long routeId, Channel replyChannel, String userName,
			boolean intercluster) {
		noOfResultExpected = ClusterDirectory.getGroupMap().size() - 1;
		this.primaryHostname = primaryHostname;
		this.routeId = routeId;
		this.dbService = new MessageService();
		this.replyChannel = replyChannel;
		this.userName = userName;
		this.intercluster = intercluster;
		if (Loadyaml.getProperty(RollbitsConstants.CLUSTER_NAME).equals(primaryHostname)) {
			needToSendPacketToPrimary = false;
		} else {
			needToSendPacketToPrimary = true;
			noOfResultExpected++;
		}
	}

	public void recievedAResult() {
		--noOfResultExpected;
	}

	public void fetchAllMessages() {
		Thread t = new Thread(new Timer(this));
		t.start();
		Map<String, Map<String, Node>> groupMap = ClusterDirectory.getGroupMap();
		if (!intercluster) {
			for (Map.Entry<String, Map<String, Node>> entry : groupMap.entrySet()) {
				if (Loadyaml.getProperty(RollbitsConstants.CLUSTER_NAME).equals(entry.getKey())) {
					continue;
				}
				Map<String, Node> nodeMap = entry.getValue();
				Node nodeOfEachGroup = null;
				List<Node> nodeList = new ArrayList<>(nodeMap.values());
				nodeOfEachGroup = nodeList.get(rand.nextInt(nodeList.size()));
				FetchMessageTask task = new FetchMessageTask(nodeOfEachGroup.getNodeIp(), nodeOfEachGroup.getPort(),
						userName, this, RollbitsConstants.INTER_CLUSTER);
				task.doTask();
			}
		}
		if (needToSendPacketToPrimary) {
			FetchMessageTask task = new FetchMessageTask(ClusterDirectory.getNodeMap().get(primaryHostname).getNodeIp(),
					ClusterDirectory.getNodeMap().get(primaryHostname).getPort(), userName, this,
					RollbitsConstants.INTERNAL);
			task.doTask();
		} else {

			List<com.sjsu.rollbits.dao.interfaces.model.Message> messages = dbService.findAllforuname(userName);

			Route.Builder rb = ProtoUtil.createMessageResponseRoute(routeId, messages, userName, true);
			collectResult(rb.getMessagesResponse().getMessagesList());
		}
	}

	@Override
	public synchronized void collectResult(List<Message> t) {
		resultList.addAll(t);
		noOfResultExpected--;
		if (noOfResultExpected == 0) {
			publishResult();
		}
	}

	@Override
	public void publishResult() {
		if (!isResultPublished) {
			System.out.println(resultList);
			replyChannel.writeAndFlush(ProtoUtil.createMessageResponseRoute2(routeId, resultList, userName, true));
		}
	}

	@Override
	public void timeout() {
		noOfResultExpected = 0;
		publishResult();

	}

}
